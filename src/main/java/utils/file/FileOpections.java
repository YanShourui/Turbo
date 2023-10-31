package utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * ClassName: FileOpections
 * Package: utils.file
 * Description:
 *
 * @Author: 闫守瑞
 * @Create: 2023/10/26 - 14:11
 * @Version: v1.0
 */
public class FileOpections {
    /**
     * 遍历文件夹并复制特定类型文件的方法
     *
     * @param folder             要遍历的文件夹
     * @param destinationFolder  目标文件夹
     * @param folderName         文件夹名称
     */
    public static void traverseFolders(File folder, File destinationFolder, String folderName) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归遍历子文件夹
                traverseFolders(file, destinationFolder, folderName);
            } else if (file.isFile() && file.getName().endsWith(".pdf")) {
                try {
                    // 判断文件名是否包含指定字符
                    if (file.getName().contains("正文H") || file.getName().contains("封面")
                            || file.getName().contains("护封")) {
                        // 创建目标文件夹
                        File destFolder = new File(destinationFolder.getAbsolutePath() + File.separator + folderName);
                        if (!destFolder.exists()) {
                            destFolder.mkdirs();
                        }
                        // 复制文件到目标文件夹
                        Path sourcePath = file.toPath();
                        Path destinationPath = new File(destFolder.getAbsolutePath() + File.separator + file.getName()).toPath();
                        Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (file.isFile() && file.getName().endsWith(".jpg")){
                // 创建目标文件夹
                File destFolder = new File(destinationFolder.getAbsolutePath() + File.separator + folderName);
                if (!destFolder.exists()) {
                    destFolder.mkdirs();
                }
                // 复制文件到目标文件夹
                Path sourcePath = file.toPath();
                Path destinationPath = new File(destFolder.getAbsolutePath() + File.separator + file.getName()).toPath();
                Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            }
        }
    }

}
