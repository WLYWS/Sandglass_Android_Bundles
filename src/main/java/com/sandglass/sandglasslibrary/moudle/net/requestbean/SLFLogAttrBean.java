package com.sandglass.sandglasslibrary.moudle.net.requestbean;

/**
 * Greated by yangjie
 * describe:log请求的数据bean
 * time:2022/12/29
 */
public class SLFLogAttrBean  {
        public String path;
        public String fileName;
        public String contentType;

        public SLFLogAttrBean (String path, String fileName, String contentType) {
            this.path = path;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public SLFLogAttrBean(){};

        @Override
        public String toString ( ) {
            return "HistiryBean{" +
                    "path='" + path + '\'' +
                    ", fileName='" + fileName + '\'' +
                    ", contentType='" + contentType + '\'' +
                    '}';
        }

        public void setPath (String path) {
            this.path = path;
        }

        public void setFileName (String fileName) {
            this.fileName = fileName;
        }

        public void setContentType (String contentType) {
            this.contentType = contentType;
        }


        public String getPath ( ) {
            return path;
        }

        public String getFileName ( ) {
            return fileName;
        }

        public String getContentType ( ) {
            return contentType;
        }

}
