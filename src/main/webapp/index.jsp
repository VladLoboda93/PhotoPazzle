<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="create_mosaic.do" method="post" enctype="multipart/form-data">
	
	<input type="file" name="img_to_process" title="фото">
	
	<br>
	<p>плитка:</p>
	<label><input type="radio" name="tile_source" value="template">из готового шаблона</label>
	<input type="text" name="tile_tag">
	<br>
	<label><input type="radio" name="tile_source" value="upload">загрузить архив</label>
	<input type="file" name="tiles_zip">
	<br>
	<input type="submit" name="submit" value="submit">
	</form>

</body>
</html>
