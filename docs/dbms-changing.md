## Смена СУБД
Приложение по умолчанию использует СУБД H2 и сохраняет данные в файл `portfolio.mv.db` в каталоге пользователя.
Если у вас не достаточно опыта или нет желания перейти на другую СУБД, пропустите этот раздел.

Возможен переход на [MariaDB](https://downloads.mariadb.org/)
([ссылка](https://downloads.mariadb.org/interstitial/mariadb-10.4.12/win32-packages/mariadb-10.4.12-win32.msi/from/http%3A//mariadb.melbourneitmirror.net/)
на дистрибутив для Windows). После установки в файле `application-conf.properties` необходимо прописать
```
spring.profiles.active=core,mariadb,conf
```
и указать логин и пароль доступа к БД
```
spring.datasource.username=root
spring.datasource.password=123456
```
После смены БД необходимо перезалить отчеты брокера. Ранее загруженные отчеты могут быть найдены в домашней директории
пользователя в папке `investbook/report-backups`.