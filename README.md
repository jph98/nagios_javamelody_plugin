JavaMelody Nagios plugin
=============

https://github.com/jph98/nagios_javamelody_plugin

This plugin can be used to capture JavaMelody Data in Nagios

The RRD files from JavaMelody are created in JRobin format, which means you can't inspect them with rrdtool without converting them.  See this page for a tutorial on JRobin:

http://oldwww.jrobin.org/api/tutorial.html

Installation
-----------

You will need to have Java 1.5 or higher installed to run this:

```
wget https://github.com/downloads/sbower/nagios_javamelody_plugin/javamelody_plugin.tar.gz
tar xzvf javamelody_plugin.tar.gz
mv javamelody_plugin/* /usr/local/nagios/libexec
```

Usage
-----

### Testing the plugin from the Command Line

Executing the JavaMelody plugin executable JAR from the command line with the following

        java -jar target/javamelody_plugin.jar -r `pwd`/rrd -tc -w 160 -c 200

* r - defines the path where the RRD files are found.  If you're running Tomcat this will be the temp directory (e.g.. /usr/local/tomcat/temp/javamelody)
* tc - specifies the key of the metric to check for.  A full list can be found in CheckMelody getDataSourceName() where the fullname is mapped to the key.  You can use the long form "threadCount" or short form "tc".
* w - specifies the warning level
* c - specifies tthe critical level

In the example above we've ommitted the -s flag.  This means the max file age check will be skipped for the RRD file.

For the values (-w 190 -c 200) a standard message will be produced:

        threadCount - 163 OK  | threadCount=163;190;200;

For the values (-w 160 -c 200) a warning message will be produced:

        threadCount - 163 WARNING  | threadCount=163;100;200;
        
For the values (-w 160 -c 162) a critical message will be produced:

        threadCount - 163 CRITICAL  | threadCount=163;100;200;

###  Commands

You can setup an nrpe config file (/etc/nrpe.d/jmelody.cfg) for the commands you want to check with the following sort of structure:

        command[check_jm_mem_app]=/usr/lib64/nagios/plugins/check_jmelody -s -r /opt/application/tomcat7/temp/javamelody/app_` hostname -f ` -uh -w 1000000000 -c 1600000000
        
Specifying the warning and critical levels.  The above checks for used heap memory.

This will call the check_melody bash script (you can find that in the support directory.

That in turn will call the JavaMelody plugin JAR file with the above values.

...

First define a command in /etc/nagios/commands.cfg

#### Local

Setting a command to check local heap

$ARG1$ -- warning level <br />
$ARG2$ -- critical level <br />
$ARG3$ -- war name (Verify this in your tomcat temp folder) <br />

```
# 'check_local_heap' command definition
define command{
        command_name    check_heap
        command_line    $USER1$/check_jmelody -r /usr/local/tomcat/temp/javamelody/$ARG3$/ -uh -w $ARG1$ -c $ARG2$
        }
```
        
#### Remote

```
# 'check_remote_heap' command definition
define command{
        command_name    check_remote_heap
        command_line    $USER1$/check_by_ssh -H $HOSTADDRESS$ -C '/usr/local/nagios/libexec/check_jmelody -r /usr/local/tomcat/temp/javamelody/$ARG3$/ -uh -w $ARG2$ -c $ARG3$'
        }
```

### Service

Then you will need to define a service in your host definition

```
# Heap
define service{
        use                             remote-service,graphed-service         ; Name of service template to use
        host_name                       hostname 
        service_description             Heap
        check_command                   check_heap!6442450944!8589934592!warfile
        }
```

Testing
-------

To run the tests:

   mvn test


Contributing
------------

1. Fork it.
2. Create a branch (`git checkout -b nagios_javamelody_plugin`)
3. Commit your changes (`git commit -am "Added Snarkdown"`)
4. Push to the branch (`git push origin nagios_javamelody_plugin`)
5. Create an [Issue][1] with a link to your branch
6. Enjoy a refreshing Diet Coke and wait


[1]: https://github.com/sbower/nagios_javamelody_plugin/issues
