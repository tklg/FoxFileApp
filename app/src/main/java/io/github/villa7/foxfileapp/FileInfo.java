package io.github.villa7.foxfileapp;

import java.text.DecimalFormat;

/**
 * Created by kluget on 5/12/2015.
 */
public class FileInfo {

    private FileItem file;
    private String oFileType;
    public String bft = "file";
    public String language = ""; //if it is code
    public String dft = "";
    public String mime = "";
    public String size = "0MB";
    private DecimalFormat fmt = new DecimalFormat("#.##");

    public FileInfo(FileItem file) {
        this.file = file;
    }
    public String getExt(String name) {
        String[] a = name.split("\\.");
        //System.out.println(a.length);
        if (a.length >= 1) {
            return a[a.length - 1];
        } else {
            return "";
        }
    }
    public String getBft() {
        return bft;
    }
    public String getDft() {
        return dft;
    }
    public String getSize() {
        return size;
    }

    public void getInfo() {
        double fSize = Double.parseDouble(file.getSize());
        if (fSize > 1000) {
            if (fSize > 1000000) {
                if (fSize > 1000000000) {
                    size = fmt.format(fSize / 1000000000) + " GB";
                } else {
                    size = fmt.format(fSize / 1000000) + " MB";
                }
            } else {
                size = fmt.format(fSize / 1000) + " KB";
            }
        } else {
            size = fmt.format(fSize) + " B";
        }
        if (file.getType().equals("folder")) {
            bft = "folder";
            dft = "Folder";
            size = "";
        } else {
            String ext = getExt(file.getName());
            /*if (ext.indexOf("/") > 0) { //"/place/thing/file.ext"
                ext = getExt(ext);
            }*/
            //System.out.println("Ext: " + ext + " from " + file.getName());
            switch (ext) {
                case "txt":
                    bft = "text";
                    dft = "Text File";
                    language = "";
                    break;
                case "log":
                    bft = "text";
                    dft = "Log File";
                    language = "";
                    break;
                case "rtf":
                    bft = "text";
                    dft = "Rich Text";
                    language = "";
                    break;
                case "js":
                    bft = "code";
                    dft = "Javascript";
                    language = "javascript";
                    break;
                case "java":
                    bft = "code";
                    dft = "Java File";
                    language = "clike"; //cm doesnt have a mode for java
                    mime = "text/x-java";
                    break;
                case "bat":
                    bft = "code";
                    dft = "Batch File";
                    language = "";
                    break;
                case "c":
                    bft = "code";
                    dft = "C File";
                    language = "clike";
                    mime = "text/x-csrc";
                    break;
                case "cs":
                    bft = "code";
                    dft = "C# File";
                    language = "clike";
                    mime = "text/x-csharp";
                    break;
                case "cpp":
                    bft = "code";
                    dft = "C++ File";
                    language = "clike";
                    mime = "text/x-c++src";
                    break;
                case "lua":
                    bft = "code";
                    dft = "LUA Script";
                    language = "lua";
                    break;
                case "md":
                    bft = "code";
                    dft = "Markdown File";
                    language = "markdown";
                    break;
                case "css":
                    bft = "code";
                    dft = "CSS File";
                    language = "css";
                    break;
                case "scss":
                    bft = "code";
                    dft = "Sass File";
                    language = "scss";
                    break;
                case "html":
                    bft = "code";
                    dft = "HTML File";
                    language = "htmlmixed";
                    break;
                case "htm":
                    bft = "code";
                    dft = "HTM File";
                    language = "htmlmixed";
                    break;
                case "php":
                    bft = "code";
                    dft = "PHP Script";
                    language = "php";
                    break;
                case "json":
                    bft = "code";
                    dft = "JSON File";
                    language = "json";
                    break;
                case "rb":
                    bft = "code";
                    dft = "Ruby File";
                    language = "ruby";
                    break;
                case "py":
                    bft = "code";
                    dft = "Python Script";
                    language = "python";
                    break;
                case "sql":
                    bft = "code";
                    dft = "SQL Script";
                    language = "sql";
                    break;
                case "vbs":
                    bft = "code";
                    dft = "Visual Basic";
                    language = "vbs";
                    break;
                case "ino":
                    bft = "code";
                    dft = "Arduino Sketch";
                    language = "clike";
                    mime = "text/x-csrc";
                    break;
                case "dat":
                    bft = "text";
                    dft = "Data File";
                    language = "text";
                    break;
                case "xml":
                    bft = "text";
                    dft = "XML Sheet";
                    language = "xml";
                    break;
                case "aif":
                    bft = "audio";
                    dft = "AIFF Audio";
                    break;
                case "m4a":
                    bft = "audio";
                    dft = "MPEG-4 Audio";
                    break;
                case "mid":
                    bft = "audio";
                    dft = "MIDI File";
                    break;
                case "mp3":
                    bft = "audio";
                    dft = "MP3 Audio";
                    break;
                case "mpa":
                    bft = "audio";
                    dft = "MPEG-2 Audio";
                    break;
                case "wav":
                    bft = "audio";
                    dft = "Waveform Audio";
                    break;
                case "wma":
                    bft = "audio";
                    dft = "Windows Audio";
                    break;
                case "avi":
                    bft = "video";
                    dft = "AVI Video";
                    break;
                case "m4v":
                    bft = "video";
                    dft = "M4V Video";
                    break;
                case "mov":
                    bft = "video";
                    dft = "Movie File";
                    break;
                case "mp4":
                    bft = "video";
                    dft = "MP3 Video";
                    break;
                case "mpg":
                    bft = "video";
                    dft = "MPEG Video";
                    break;
                case "wmv":
                    bft = "video";
                    dft = "Windows Video";
                    break;
                case "bmp":
                    bft = "image";
                    dft = "Bitmap Image";
                    break;
                case "jpg":
                    bft = "image";
                    dft = "JPEG Image";
                    break;
                case "png":
                    bft = "image";
                    dft = "PNG Image";
                    break;
                case "psd":
                    bft = "image";
                    dft = "Photoshop Document";
                    break;
                case "tga":
                    bft = "image";
                    dft = "TARGA Image";
                    break;
                case "gif":
                    bft = "image";
                    dft = "Animated GIF";
                    break;
                case "svg":
                    bft = "image";
                    dft = "Scalable Vector";
                    break;
                case "ai":
                    bft = "image";
                    dft = "Illustrator Document";
                    break;
                case "zip":
                    bft = "zip";
                    dft = "ZIP Archive";
                    break;
                case "gz":
                    bft = "zip";
                    dft = "GNU Archive";
                    break;
                case "rar":
                    bft = "zip";
                    dft = "RAR Archive";
                    break;
                case "pkg":
                    bft = "zip";
                    dft = "Package Archive";
                    break;
                case "7z":
                    bft = "zip";
                    dft = "7Zip Archive";
                    break;
                case "pdf":
                    bft = "pdf";
                    dft = "Adobe PDF";
                    break;
                default:
                    bft = "file";
                    dft = ext.toUpperCase() + " File";
                    break;
            }
        }
    }
}
