#this script invokes CCE API to create an application#
#please ensure you have an existing cluster in your account#
########  Add your environment variable here #########
OS_USERNAME=
OS_TENANT_NAME=eu-de
OS_PROJECT_NAME=eu-de
OS_AUTH_URL=https://iam.eu-de.otc.t-systems.com/v3
OS_PASSWORD=
OS_USER_DOMAIN_NAME=
LANG=en_US.UTF-8
com=`echo $OS_AUTH_URL | awk -F '//iam.' '{print $2}' | awk -F '/v' '{print $1}'`
###################################
echo "get tenant_id and token"
#########  get tenant_id  #########
tenant_id=`curl -i -k -X POST https://iam.$com/v3/auth/tokens -H "Content-Type:application/json" -d "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\":{\"user\":{\"name\":\"$OS_USERNAME\",\"password\":\"$OS_PASSWORD\",\"domain\":{\"name\":\"$OS_USER_DOMAIN_NAME\"}}}},\"scope\":{\"project\":{\"name\":\"$OS_PROJECT_NAME\"}}}}" | awk -F '"id":"' '{print $2}' | awk -F '","' '{print $1}' | grep .`
echo "tenant_id is: $tenant_id"
#########  get token  #########
TOKEN=`curl -i -k -X POST https://iam.$com/v3/auth/tokens -H "Content-Type:application/json" -d "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\":{\"user\":{\"name\":\"$OS_USERNAME\",\"password\":\"$OS_PASSWORD\",\"domain\":{\"name\":\"$OS_USER_DOMAIN_NAME\"}}}},\"scope\":{\"project\":{\"name\":\"$OS_PROJECT_NAME\"}}}}" | grep X-Subject-Token: | awk -F  ':' '{print $2}' | grep .`
echo -e "get token:"
echo $TOKEN
#########  get app id  ####### 
echo -e "CCE app id: $1"
echo -e "CCE cluster id: $2"
### get cluster credentials ###
echo "get credentials of cluster $2"
curl -v -k https://cce.$com/api/v1/clusters/$2/certificates -H "Content-Type:application/json" -H "X-Auth-Token:$TOKEN"

### create rc ###
curl -i -k -X POST https://cce.$com/api/v1/namespaces/default/replicationcontrollers -H "Content-Type:application/json" -H "X-Auth-Token:$TOKEN" -H "X-Cluster-UUID:$2" -d "{\"metadata\":{\"name\":\"rc$1\",\"labels\":{\"cce\/appgroup\":\"app$1\"}},\"apiVersion\":\"v1\",\"kind\":\"ReplicationController\",\"spec\":{\"template\":{\"metadata\":{\"name\":\"rc$1\",\"labels\":{\"cce\/appgroup\":\"app$1\"}},\"spec\":{\"containers\":[{\"image\":\"nginx\",\"imagePullPolicy\":\"IfNotPresent\",\"name\":\"nginx\",\"ports\":[{\"protocol\":\"TCP\",\"containerPort\":80}]}]}},\"replicas\":2,\"selector\":{\"cce\/appgroup\":\"app$1\"}}}"
curl -i -k -X POST https://cce.$com/api/v1/namespaces/default/services -H "Content-Type:application/json" -H "X-Auth-Token:$TOKEN" -H "X-Cluster-UUID:$2" -d "{\"metadata\":{\"name\":\"service$1\"},\"apiVersion\":\"v1\",\"kind\":\"Service\",\"spec\":{\"selector\":{\"cce\/appgroup\":\"app$1\"},\"type\":\"NodePort\",\"ports\":[{\"protocol\":\"TCP\",\"port\":80,\"targetPort\":80,\"nodePort\":3000$1}]}}"
