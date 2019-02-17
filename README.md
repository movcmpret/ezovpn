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
2. Add openvpn to your PATH ([Tutorial](https://support.nordvpn.com/Connectivity/Windows/1162364372/How-to-put-OpenVPN-on-your-command-path-on-Windows.htm))
3. Execute the .exe File.
4. Enjoy ezOVPN :-)

# 'I want to know what I am doing' guide
__Important:__ Unfortunately, ezOVPN needs to be executed as root, because it is interacting with the OpenVPN CLI, which creates a TUN/TAP interface and a local TCP server called [Management Interface](https://openvpn.net/community-resources/management-interface/). If you're uncomfortable with it, just give the executing user permissions to openvpn and you should be fine without running the whole software as root.

The `preconditions.sh` script installs the latest openVPN client via apt, creates an executable script, which basically only executes `java -jar /path/to/jarfile.jar` and a symlink in /usr/bin/ezovpn to this script. Furthermore, it enables all users to execute it as sudo, which is perhaps a vulnerability in your system.

__ezOVPN is still an unstable and insecure software, so please be careful and don't use this software, unless you operate on a safe environment. Keep in mind, that you use this software at your own risk. It is provided without any kind of warranty.__



# Credits

Joda.org - JodaTime

Google Inc. - GSON 

openvpn.com - OpenVPN

nordvpn.com - NordVPN

Icons: Atomic Lotus, Bogdan Rosu Creative

Logo by Don Theonardo Liesanni



Copyright © movcmpret.com, released under GPLv3

Questions to `movcmpret -[at]- protonmail -[dot]- com`
