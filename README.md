访问路径：http://localhost:8080/index.html 

![image](https://github.com/user-attachments/assets/b69936e6-5c84-4492-be9b-8ad415c271ac)

测试账号 
	username: user
	password: password
登陆 访问的路径（http://localhost:8080/authenticate） 做 JWT token 的生成
返回：

```json
{  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzI4NzIxNTEyLCJpYXQiOjE3Mjg3MjE0ODJ9.SmnO6Qp7f5pvCkhA7XA6LmHF9Tn6W-PS0PafhPcIeSI",  // 获取 资源的token
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNzI4NzIxNTQyLCJpYXQiOjE3Mjg3MjE0ODJ9.JTE2EICLyxAAWbmHQQ0QM13R_cNhDg4AkkVF4abbcKo", // 更新token 的 token
  "expiresTime": 30, // 资源token的过期时间
  "refreshExpiresTime": 60 // 更新token的token 过期时间
}
```
![image](https://github.com/user-attachments/assets/ed917e12-1530-44e6-a5c4-aa4df5e91836)

Get Resource 在通过 token 去请求访问资源
Refresh Token 通过 携带 refreshToken 去请求 获取 新的 token 同时 更新 获取 资源的 token 

