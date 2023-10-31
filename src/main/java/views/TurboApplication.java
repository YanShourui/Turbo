package views;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static utils.excel.ExcelOpections.readFirstCell;
import static utils.excel.ExcelOpections.readSecondColumn;
import static utils.file.FileOpections.traverseFolders;

public class TurboApplication extends Application {
    TextArea textArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FunctionPage functionPage = new FunctionPage(primaryStage);
        Scene scene = new Scene(functionPage, 450, 460);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Turbo");
        primaryStage.show();
    }

    private class FunctionPage extends BorderPane {
        String excelFilePath;

        public FunctionPage(Stage primaryStage) {
            setPadding(new Insets(10));
            setStyle("-fx-background-color: white;");

            textArea.setPrefRowCount(15);
            Button excelButton = new Button("选择Excel文件");
            excelButton.setStyle("-fx-pref-width: 300px; -fx-pref-height: 35px; -fx-border-radius: 5px;-fx-font-size: 16px;-fx-background-color: white;-fx-border-color: black;");
            excelButton.setOnAction(e -> {
                excelFilePath = selectExcelFile(primaryStage);
                if (excelFilePath != null) {
                    executeOperations();
                }
            });

            VBox vBox = new VBox();
            vBox.getChildren().addAll(excelButton);
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(20);

            setCenter(vBox);
            setTop(textArea);
            textArea.setText("注意：\n" + "  1、运行前务必保证存在：D:\\001AThe_finished_text_and_cover_file_have_been_sorted文件夹\n" + "  2、下载完成后会显示【程序运行结束!!!】");
            textArea.setStyle("-fx-text-fill: gray; -fx-font-size: 18px;-fx-background-color: black;");
            textArea.setEditable(false);
        }

        private String selectExcelFile(Stage stage) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Excel File");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx");
            fileChooser.getExtensionFilters().add(extFilter);
            File selectedFile = fileChooser.showOpenDialog(stage);
            return selectedFile != null ? selectedFile.getAbsolutePath() : null;
        }

        private void executeOperations() {
            textArea.clear();
            textArea.setStyle("-fx-text-fill: black; -fx-font-size: 18px;");

            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    String resPath = readFirstCell(excelFilePath);
                    String destPath = "D:\\001AThe_finished_text_and_cover_file_have_been_sorted";
                    List<String> texts = List.of(readSecondColumn(excelFilePath));
                    for (String text : texts) {
                        File resFile = new File(resPath);
                        File destFile = new File(destPath);
                        File targetFolder = null;

                        File[] folders = resFile.listFiles();
                        for (File folder : folders) {
                            if (folder.isDirectory() && folder.getName().equals(text)) {
                                targetFolder = folder;
                                break;
                            }
                        }

                        if (targetFolder != null) {
                            try {
                                traverseFolders(targetFolder, destFile, text);
                                updateMessage(">> 复制完成：" + text);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            updateMessage("未找到指定文件夹：" + text);
                        }
                    }
                    return null;
                }

                @Override
                protected void updateMessage(String message) {
                    Platform.runLater(() -> textArea.appendText(message + "\n"));
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    Platform.runLater(() -> textArea.appendText("\n【程序运行结束】"));
                }

                @Override
                protected void failed() {
                    super.failed();
                    Platform.runLater(() -> textArea.appendText(getException().toString()));
                }
            };

            new Thread(task).start();
        }
    }
}
