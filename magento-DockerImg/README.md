# Sample e-shop image based on Magento e-commerce platform

This folder is used to build e-shop demo environment with Docker instance as frontend tier and save data in MySQL database.

User can deploy frontend to CaaS platform (Kubernetes or Cloud Container Engine), and create MySQL VM or RDS instance and setup database based on the SQL script.

Preparation steps:

###Step 1: Build Docker image.  

Run cmd in this folder:

<pre><code>docker build -t eshop:latest .</code></pre>

###Step 2: Check the image ID.  

Run cmd:

<pre><code>dockcr ps</code></pre>

###Step 3: (optional) Upload the Docker image to Docker registry.

###Step 4: Create MySQL server instance.

Obtain following parameters: MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD, MYSQL_DATABASE, (optional)MYSQL_PORT

###Step 5: Setup database.  

Run cmd:

<pre><code>mysql -h $MYSQL_HOST -P$MYSQL_PORT -u $MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE < magento_sample_data_for_1.9.1.0.sql</code></pre>

###Step 6: Run Docker instance from the image.  

Run cmd:

<pre><code>docker run -p 80:80 eshop:latest</code></pre>

In CaaS platform, you should use platform API to create instances.

###Step 7. Visit application and setup Magento.

Visit Docker instance URL to setup Magento.

Note: setup database with corrent parameters from Step 4.

*When everything is set up, you can visit Docker instance URL to start using the e-shop!*

An snapshot of the app is in eshop.png
