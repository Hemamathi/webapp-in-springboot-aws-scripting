#!/bin/bash
AWS_REGION="us-east-1"

VPC_CIDR="10.0.0.0/16"

SUBNET_PUBLIC_CIDR="10.0.1.0/24"
SUBNET_PUBLIC_CIDR2="10.0.2.0/24"
SUBNET_PUBLIC_CIDR3="10.0.3.0/24"
SUBNET_PUBLIC_AZ="us-east-1a"
SUBNET_PUBLIC_AZ2="us-east-1b"
SUBNET_PUBLIC_AZ3="us-east-1c"
SUBNET_PUBLIC_NAME="10.0.1.0 - us-east-1a"
SUBNET_PUBLIC_NAME2="10.0.2.0 - us-east-1b"
SUBNET_PUBLIC_NAME3="10.0.3.0 - us-east-1c"

port22CidrBlock="0.0.0.0/0"

read -p "Enter your Network STACK_NAME: " name
stack_name="$name-csye6225-vpc"
securityGroupName="$name-MySecurityGroup"
route_table="$name-routetable"
VPC_NAME="$name-MyVPC"
ig_const="$name-ig"
subnet="$name-subnet"

# Create VPC
echo "Creating VPC in preferred region..."
VPC_ID=$(aws ec2 create-vpc   --cidr-block $VPC_CIDR  --query 'Vpc.{VpcId:VpcId}'  --output text   --region $AWS_REGION)
if [ -z '$VPC_ID' ];
then
	echo 'error in creating vpc'
      exit
else
echo "  VPC ID '$VPC_ID' CREATED in '$AWS_REGION' region."
fi
# # Add Name tag to VPC
$(aws ec2 create-tags   --resources $VPC_ID   --tags "Key=Name,Value=$stack_name$VPC_NAME"   --region $AWS_REGION)
echo "  VPC ID '$VPC_ID' NAMED as '$VPC_NAME'."
# Create Public Subnet 1
echo "Creating Public Subnet 1..."
SUBNET_PUBLIC_ID1=$(aws ec2 create-subnet   --vpc-id $VPC_ID   --cidr-block $SUBNET_PUBLIC_CIDR   --availability-zone $SUBNET_PUBLIC_AZ   --query 'Subnet.{SubnetId:SubnetId}'   --output text  --region $AWS_REGION)
if [ -z '$SUBNET_PUBLIC_ID1' ];
then
echo 'error in creating subnet 01'
exit
else
echo "  Subnet ID '$SUBNET_PUBLIC_ID1' CREATED in '$SUBNET_PUBLIC_AZ'" 
fi
# Add Name tag to Public Subnet 1
aws ec2 create-tags   --resources $SUBNET_PUBLIC_ID1   --tags "Key=Name,Value=$subnet"   --region $AWS_REGION
echo "  Subnet ID $SUBNET_PUBLIC_ID1 NAMED as $SUBNET_PUBLIC_NAME."


# Create Public Subnet 2
echo "Creating Public Subnet 2.."
SUBNET_PUBLIC_ID2=$(aws ec2 create-subnet   --vpc-id $VPC_ID  --cidr-block $SUBNET_PUBLIC_CIDR2  --availability-zone $SUBNET_PUBLIC_AZ2 --query 'Subnet.{SubnetId:SubnetId}'   --output text   --region $AWS_REGION)
if [ -z '$SUBNET_PUBLIC_ID2' ];
then
echo 'error in creating subnet 02'
exit
else
echo "Subnet ID $SUBNET_PUBLIC_ID2 CREATED in $SUBNET_PUBLIC_AZ2"
fi
# Add Name tag to Public Subnet
aws ec2 create-tags   --resources $SUBNET_PUBLIC_ID2   --tags "Key=Name,Value=$subnet" --region $AWS_REGION
echo "  Subnet ID '$SUBNET_PUBLIC_ID2' NAMED as $SUBNET_PUBLIC_NAME2."

# Create Public Subnet 3
echo "Creating Public Subnet 3..."
SUBNET_PUBLIC_ID3=$(aws ec2 create-subnet   --vpc-id $VPC_ID   --cidr-block $SUBNET_PUBLIC_CIDR3   --availability-zone $SUBNET_PUBLIC_AZ3  --query 'Subnet.{SubnetId:SubnetId}'   --output text   --region $AWS_REGION)
if [ -z '$SUBNET_PUBLIC_ID3' ];
then
	echo 'error in creating subnet 03'
	exit
else
echo "  Subnet ID $SUBNET_PUBLIC_ID3 CREATED in $SUBNET_PUBLIC_AZ3" 
fi
# Add Name tag to Public Subnet
aws ec2 create-tags   --resources $SUBNET_PUBLIC_ID3   --tags "Key=Name,Value=$subnet"   --region $AWS_REGION
echo "  Subnet ID '$SUBNET_PUBLIC_ID3' NAMED as '$SUBNET_PUBLIC_NAME3'."

# Create Internet gateway
echo "Creating Internet Gateway..."
IGW_ID=$(aws ec2 create-internet-gateway   --query 'InternetGateway.{InternetGatewayId:InternetGatewayId}'   --output text   --region $AWS_REGION)
if [ -z '$IGW_ID' ];
then
	echo 'error in creating internet gateway'
	exit
else
echo "  Internet Gateway ID '$IGW_ID' CREATED."
fi
# Attach Internet gateway to your VPC
aws ec2 attach-internet-gateway   --vpc-id $VPC_ID   --internet-gateway-id $IGW_ID   --region $AWS_REGION
echo "  Internet Gateway ID '$IGW_ID' ATTACHED to VPC ID '$VPC_ID'."
aws ec2 create-tags --resources $IGW_ID --tags "Key=Name,Value=$ig_const"
# Create Route Table
echo "Creating Route Table..."
ROUTE_TABLE_ID=$(aws ec2 create-route-table  --vpc-id $VPC_ID  --query 'RouteTable.{RouteTableId:RouteTableId}'   --output text   --region $AWS_REGION)
if [ -z '$ROUTE_TABLE_ID' ];
then
	echo 'error in creating route table'
	exit
else
echo "  Route Table ID '$ROUTE_TABLE_ID' CREATED."
fi
aws ec2 create-tags --resources $ROUTE_TABLE_ID --tags "Key=Name,Value=$route_table"
# Create route to Internet Gateway
RESULT01=$(aws ec2 create-route   --route-table-id $ROUTE_TABLE_ID   --destination-cidr-block 0.0.0.0/0   --gateway-id $IGW_ID   --region $AWS_REGION)
  if [ -z 'RESULT01' ];
  then
	  echo 'error in creating internet gateway'
	  exit
  else
echo "  Route to '0.0.0.0/0' via Internet Gateway ID '$IGW_ID' ADDED to route table"
fi
# Associate Public Subnet with Route Table
RESULT=$(aws ec2 associate-route-table --subnet-id $SUBNET_PUBLIC_ID1 --route-table-id $ROUTE_TABLE_ID  --region $AWS_REGION)
echo "Public Subnet ID $SUBNET_PUBLIC_ID1 ASSOCIATED with $ROUTE_TABLE_ID" 

# Associate Public Subnet with Route Table
RESULT2=$(aws ec2 associate-route-table  --subnet-id $SUBNET_PUBLIC_ID2   --route-table-id $ROUTE_TABLE_ID  --region $AWS_REGION)
echo "  Public Subnet ID $SUBNET_PUBLIC_ID2 ASSOCIATED with $ROUTE_TABLE_ID" 
 

# Associate Public Subnet with Route Table
RESULT3=$(aws ec2 associate-route-table    --subnet-id $SUBNET_PUBLIC_ID3   --route-table-id $ROUTE_TABLE_ID   --region $AWS_REGION)
echo "  Public Subnet ID $SUBNET_PUBLIC_ID3 ASSOCIATED with $ROUTE_TABLE_ID" 
aws ec2 create-route --route-table-id $ROUTE_TABLE_ID --destination-cidr-block 0.0.0.0/0 --gateway-id $IGW_ID 

#create security group
security_response=$(aws ec2 create-security-group  --group-name "$securityGroupName" --description "Public:$securityGroupName"  --vpc-id $VPC_ID --output text)
if [ -z '$security_response' ];
then 
echo 'error creating security group'
exit
else
echo "'$securityGroupName' '$security_response'."
fi

#name the security group
aws ec2 create-tags   --resources "$security_response"   --tags Key=Name,Value="$securityGroupName"


#enable port 22
security_response2=$(aws ec2 authorize-security-group-ingress  --group-id "$security_response"  --protocol tcp --port 22  --cidr "$port22CidrBlock")
 echo " Port 22 with Inbound Rule '$port22CidrBlock' Add to '$security_response'."

#enable port 80
security_response3=$(aws ec2 authorize-security-group-ingress  --group-id "$security_response"  --protocol tcp --port 80  --cidr "$port22CidrBlock")
  echo " Port 80 with Inbound Rule '$port22CidrBlock' Add to '$security_response'."
