CREATE USER multicloud IDENTIFIED BY "${multicloud_password}";

GRANT CONNECT, RESOURCE TO multicloud;
GRANT UNLIMITED TABLESPACE TO multicloud;
