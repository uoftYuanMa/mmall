
<%--防止乱码--%>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<html>
<body>
<h2>Tomcat1!!!</h2>
<h2>Tomcat1!!!</h2>
<h2>Tomcat1!!!</h2>
<h2>Hello World!</h2>

springMVC上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="springMVC上传文件">
</form>

richtext_img_upload
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="富文本图片上传文件" >
</form>
</body>
</html>
