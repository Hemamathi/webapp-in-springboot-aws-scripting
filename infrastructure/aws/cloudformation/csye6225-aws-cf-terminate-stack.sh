#!/bin/bash
read -p "Enter your STACK_NAME to delete: " name
stackName="$name-csye6225-network"

mapfile -t my_array < <( aws cloudformation list-stacks --stack-status-filter CREATE_COMPLETE | jq -r '.[] | .[] | .StackName' )
for i in "${my_array[@]}";
do
	if [ "$i" == "$stackName" ] ; then
        aws cloudformation delete-stack --stack-name $stackName
		RESULT=$?
		printf "\n\n"
		printf "######################  Deleting the stack  ###################### \n\n"
		printf "......................  Please wait  ...................... \n"
		aws cloudformation wait stack-delete-complete --stack-name $stackName
		if [ $RESULT -eq 0 ]; then
			printf "\n"
			printf "......................  Deleted VPC '$stackName' ...................... \n\n"
			printf "......................  Deleted 3 Subnets  ...................... \n\n"
			printf "......................  Deleted InternetGateway  ...................... \n\n"
			printf "......................  Deleted RouteTable  ...................... \n\n"
			printf "......................  Deleted Routes  ...................... \n\n"
			printf "......................  Deleted Routes  ...................... \n\n"
		    printf "<<<<<<<<<<<<<<<<<<<<<<  Stack deleted successfully  >>>>>>>>>>>>>>>>>>>>>>"
		    printf "\n\n"
			exit
		else
			printf "\n\n"
			printf "!!!!!!!!!!!!!!!!!!!!!!  Stack deletion failed  !!!!!!!!!!!!!!!!!!!!!!"
			printf "\n\n"
			exit
		fi
    fi
done
		printf "\n\n......................  Stack doesn't exists, please specify a correct name ...................... \n\n"
        exit    
