# https://ezovpn.movcmpret.com

# What is ezOVPN?
ezOVPN - or easy OVPN - is an open source cross-platform OpenVPN GUI client, that allows you to easily manage your configurations. 


# Qickstart 
The quickstart guide helps you to immediately jump into ezOVPN. 

## Ubuntu 
1. Download the latest [Ubuntu package](https://ezovpn.movcmpret.com/#containerDownload). 
2. Execute `./preconditions.sh` without root permissions. You will be asked for your sudo password. 
3. Create a ezOVPN.desktop file `sudo touch ezOVPN.desktop && sudo chmod 775` and follow [this guide](https://developer.gnome.org/integration-guide/stable/desktop-files.html.en=). Set `Exec=sudo ezovpn` and `Icon=/path/to/your_release/ezOVPN_logo.png`
4. Enjoy ezOVPN :-)

## Windows
1. Download the latest [Windows package](https://ezovpn.movcmpret.com/#containerDownload). 
2. Execute the .exe File.
3. Enjoy ezOVPN :-)

# 'I want to know what I am doing' guide
__Important:__ Unfortunately, ezOVPN needs to be executed as root, because it is interacting with the OpenVPN CLI, which creates a TUN/TAP interface and a local TCP server called [Management Interface](https://openvpn.net/community-resources/management-interface/). 

__ezOVPN is still an unstable and unsecure software, so please be careful and don't use this software, unless you operate on a safe environment. Keep in mind, that you use this software at your own risk. I don't give any warartny__



# Credits

Joda.org - JodaTime

Google Inc. - GSON 

openvpn.com - OpenVPN

nordvpn.com - NordVPN




Copyright © movcmpret.com, released under GPLv3

Questions to `movcmpret -[at]- protonmail -[dot]- com`
