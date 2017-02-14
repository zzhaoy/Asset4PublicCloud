# Sample e-shop image based on Magento e-commerce platform

This folder is used to build e-shop demo environment with Docker instance as frontend tier and save data in MySQL database.

User can deploy frontend to CaaS platform (Kubernetes or Cloud Container Engine), and create MySQL VM or RDS instance and setup database based on the SQL script.

Preparation steps:
1. Build Docker image.  Run cmd in this folder:

<pre><code>docker build -t eshop:latest .</code></pre>

2. Check the image ID.  Run cmd:

<pre><code>dockcr ps</code></pre>

3. (optional) Upload the Docker image to Docker registry.

4. Setup database.  Run cmd:

<pre><code>mysql -h $MYSQL_HOST -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE < magento_sample_data_for_1.9.1.0.sql</code></pre>

5. Run Docker instance from the image.  Run cmd:

<pre><code>docker run -p 80:80 eshop:latest</code></pre>

6. Visit application at http://<hostname>:80