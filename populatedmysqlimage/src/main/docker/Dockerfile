FROM mariadb:5.5.44

ADD run_db init_db dump.sql /tmp/
RUN chmod u+x /tmp/init_db /tmp/run_db

# Skip reverse DNS lookup of clients
RUN sed -i '/skip-external-locking/a \skip-name-resolve' /etc/mysql/my.cnf

# init_db will import the dump, then stop mysqld, and finally package the /var/lib/mysql directory to 'default_mysql_db.tar.gz'
RUN /tmp/init_db

# run_db starts mysqld, but first it checks to see if the /var/lib/mysql directory is empty. 
# if it is, it is seeded with 'default_mysql_db.tar.gz' before the mysql is fired up
ENTRYPOINT "/tmp/run_db"
