<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
</head>
<body onload='document.loginForm.loginHandle.focus();'>
	<h3>Login with Username and Password</h3>
	<form name='loginForm' action='/login' method='POST'>
		<table>
			<tr>
				<td>User:</td>
				<td><input type='text' name='loginHandle' value=''></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type='password' name='password' value='test' /></td>
			</tr>
			<tr>
				<td colspan='2'><input name="submit" type="submit"
					value="Login" /></td>
			</tr>
		</table>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
	<h4>Register with Username</h4>
	<form name='registerForm' action='/api/account/register' method='POST'>
		<table>
			<tr>
				<td>User:</td>
				<td><input type='text' name='loginHandle' value=''></td>
			</tr>
			<!--                <tr>
                    <td>Password:</td>
                    <td>
                        <input type='password' name='password'/>
                    </td>
                </tr>-->
			<tr>
				<td colspan='2'><input name="submit" type="submit"
					value="Register" /></td>
			</tr>
		</table>
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
</body>
</html>