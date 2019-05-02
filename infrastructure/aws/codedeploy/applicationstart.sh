#!/bin/bash
echo "#!/bin/bash" > /tmp/tempFile.sh
echo "source /etc/profile" >> /tmp/tempFile.sh
sudo chmod 777 /tmp/tempFile.sh
echo "APP_PORT=\`sudo lsof -t -i:80\`" >> /tmp/tempFile.sh
echo "sudo kill -9 \$APP_PORT" >> /tmp/tempFile.sh
echo "cd /var" >> /tmp/tempFile.sh
#echo "sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -c file:/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.json -s" >> /tmp/tempFile.sh
echo "sudo systemctl restart amazon-cloudwatch-agent" >> /tmp/tempFile.sh
echo "sudo java -jar noteapp-0.0.1-SNAPSHOT.jar --env=cloud --server.port=80 --spring.datasource.url=jdbc:mysql://\$DB_HOST:\$DB_PORT/csye6225 --spring.datasource.username=\$DB_USERNAME --spring.datasource.password=\$DB_PASSWORD --cloud.isLocal=false --bucket.name=\$S3_BUCKET --cloud.snsTopic=\$SNS_TOPIC --logging.file=/home/centos/logs/csye6225-webapp.log" >> /tmp/tempFile.sh
cd /tmp/
./tempFile.sh > run.out 2> run.err &