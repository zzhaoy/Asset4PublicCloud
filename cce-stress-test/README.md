#Stress test scripts for Cloud Container Engine

This folder is used to store the stress test script of CCE.  The typical test case is creating 100 eshop applications in a CCE cluster.

##Pre-reqs

* Java SDK is installed

* CCE account is available and 1 cluster is ready for use

* eshop Docker image has been uploaded to account's private registry

* Kubectl for CCE is setup with correct parameters (AK, SK, cluster ID)

##Build the java file

Run following command in current folder:

<pre><code>javac -Xlint:unchecked -classpath .:json-simple-1.1.1.jar magentobatch/GenerateJSON.java</code></pre>

##Run test cases

You can execute existing shell scripts, e.g.

<pre><code>sh ./create-eshop-100.sh</code></pre>

to create 100 eshop applications.

You can also customize the parameters for stress test, to check all parameters, run:

<pre><code>java -classpath .:json-simple-1.1.1.jar magentobatch.GenerateJSON
</code></pre>