# HC 小区管理系统前段安装教程
## 一、项目构建
### 1. 安装依赖
```bash
cd MicroCommunityWebVue
npm install
```
### 2. 生产环境构建
```bash
npm run build
```
构建完成后会生成dist文件夹，里面包含所有静态文件。
## 二、服务器准备
### 1. 上传dist文件夹到服务器
```bash
scp -r dist/ user@your-server-ip:/home/data/web/propertyWeb/
```
## 三、Nginx配置
properyWeb.conf
```
server {
	listen       80;
	server_name  demo.homecommunity.cn;
       
	location / {
	    root   /home/data/web/propertyWeb/dist;
	}
	location /callComponent
	{
	  add_header 'Access-Control-Allow-Origin' '*';
	  proxy_pass   http://192.168.1.109:8008;
	}
	location /app
	{

	  add_header 'Access-Control-Allow-Origin' '*';
	  proxy_pass  http://192.168.1.109:8008;
	}
}
```