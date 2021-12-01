#!/bin/bash

app_name="docker-idle-start-stopper"

rm -rf /opt/$app_name
mkdir /opt/$app_name
cp ./diss.jar /opt/$app_name/diss.jar

echo "
#!/bin/bash
java -jar /opt/$app_name/diss.jar
" > /opt/$app_name/diss

rm /etc/systemd/system/diss.service

echo "
[Unit]
Description=$app_name service

[Service]
User=$SUDO_USER
ExecStart=/bin/bash /opt/$app_name/diss

SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
" > /etc/systemd/system/diss.service

chown -R $SUDO_USER:$SUDO_USER /opt/$app_name
chmod 755 /opt/$app_name/diss

systemctl daemon-reload
systemctl enable diss.service
systemctl start diss
systemctl status diss