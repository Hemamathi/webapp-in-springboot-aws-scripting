#!/bin/bash
read -p "Enter your Network STACK_NAME: " name
name="$name"
stackName="$name-csye6225-network"

##mapfile -t my_array < <( aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE | jq -r '.[] | .[] | .StackName' )
##for i in "${my_array[@]}";
##do
 ##   if [ "$i" == "$stackName" ] ; then
   ##     printf "\n\n......................  Stack already exists, please use a different name ...................... \n\n"
    ##    exit
    ##fi
##done
printf "......................  Please wait  ...................... \n"
    	aws cloudformation create-stack --stack-name $stackName --template-body file://csye6225-cf-networking.json --parameters ParameterKey=Name,ParameterValue=$name ParameterKey=VPCName,ParameterValue=$name-csye6225-vpc ParameterKey=PublicRouteTableName,ParameterValue=$name-csye6225-rt ParameterKey=PublicSubnet1,ParameterValue=$name-csye6225-subnet1 ParameterKey=PublicSubnet2,ParameterValue=$name-csye6225-subnet2 ParameterKey=PublicSubnet3,ParameterValue=$name-csye6225-subnet3 ParameterKey=InternetGateway,ParameterValue=$name-csye6225-internetgateway
		aws cloudformation wait stack-create-complete --stack-name $stackName
		if [ $? -eq 0 ]; then
			printf "\n\n"
			printf "######################  Creating Stack, VPC, Subnets, InternetGateway, RouteTable and Routes  ###################### \n"
			printf "\n\n"
			printf "......................  Created VPC '$stackName' ...................... \n\n"
			printf "......................  Created 3 Subnets  ...................... \n\n"
			printf "......................  Created InternetGateway  ...................... \n\n"
			printf "......................  Created RouteTable  ...................... \n\n"
			printf "......................  Created Routes  ...................... \n\n"
			printf "......................  Associated Routes  ...................... \n\n"
			printf "<<<<<<<<<<<<<<<<<<<<<<  Stack created successfully  >>>>>>>>>>>>>>>>>>>>>> \n\n"
    		printf "\n\n"
    		exit
		else
			printf "\n\n"
    		printf "!!!!!!!!!!!!!!!!!!!!!!  Stack creation failed  !!!!!!!!!!!!!!!!!!!!!! \n"
    		printf "\n\n"
    		exit
		fi
##		printf "\n"	
