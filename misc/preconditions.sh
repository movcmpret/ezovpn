#!/bin/bash
set -e

#
# This script creates an executable file and a symlink to it.
# It will be accessable by the name which is defined in the 
# symlink constant.
#
# by movcmpret 2018
#
clear
user=$USER
[[ "$user" == "root" ]] && echo -e "\e[31mPlease dont exeute as root. You will be asked for your password from sudo\e[39m" && exit 1
###Constants
symlink="/usr/bin/ezovpn"
current_path="$PWD"
executable_filename="ezovpn_client.sh"

###install openvpn 
sudo echo "Installing apt package openvpn "
sudo apt-get update && sudo apt-get -y install openvpn

### get full path of executable
JAR=$(find . -name "ezOVPN*.jar" -print | cut -c 2-)
fullpath=$current_path$JAR

###create execute script
sudo echo "Creating execute script for $fullpath "

sudo echo "#!/bin/bash" >  $executable_filename
sudo echo "#script and software provided by movcmpret" >>  $executable_filename
sudo echo "#" >>  $executable_filename
sudo echo "java -jar $fullpath" >>  $executable_filename
#permissions
sudo chmod 775 $executable_filename
echo -e "\e[32mExecute script $executable_filename created\e[39m "

###symlink
#delete old symlink if exists
if [ -f "$symlink" ]
then
sudo rm $symlink
echo -e "\e[31mRemoved old symlink\e[39m"
fi
#create symlink
sudo ln -s $current_path/$executable_filename $symlink
echo -e "\e[32mAdded new symlink in $symlink (to $executable_filename)\e[39m"

###create permissions
#alter sudoers
echo "Adding to sudoers: $user ALL= NOPASSWD: /usr/bin/ezovpn"

sudo grep -qF "$user ALL= NOPASSWD: /usr/bin/ezovpn" /etc/sudoers || sudo echo "$user ALL= NOPASSWD: /usr/bin/ezovpn" | sudo EDITOR='tee -a' visudo




