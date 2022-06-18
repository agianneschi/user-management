create user "user-mgmt-user" with encrypted password 'user-mgmt-user'; 
create database "user-mgmt" with owner "user-mgmt-user";
grant all privileges on database "user-mgmt" to "user-mgmt-user";
