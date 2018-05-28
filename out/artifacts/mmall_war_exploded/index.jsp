<html>
<%--<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>--%>
<body>
<h2>Hello mmall!</h2>
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="SpringMVC上传文件">
</form>
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="richtext_img_upload_file" />
    <input type="submit" value="富文本图片上传文件">
</form>
</body>
</html>
