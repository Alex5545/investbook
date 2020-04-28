Прежде всего, спасибо, что нашли время внести свой вклад!:+1::tada:

#### Оглавление
[Как я могу внести вклад?](#как-я-могу-внести-вклад)
- [Сообщение об ошибках](#сообщение-об-ошибках)
- [Предложение об улучшении](#предложение-об-улучшении)
- [Ваш первый вклад в качестве контрибьютора](#ваш-первый-вклад-в-качестве-контрибьютора)
- [Добавление функциональности и исправление ошибок](#добавление-функциональности-и-исправление-ошибок)
- [Создание документации](#создание-документации)
- [Перевод документации](#перевод-документации)

[Использование лейблов](#использование-лейблов)

## Как я могу внести вклад?

### Сообщение об ошибках
Перед созданием заявки об ошибках, проверьте список заявок, возможно дублирующая заявка уже существует.
Если вы нашли закрытую заявку об ошибке, которая похожа на вашу, не нужно ее переоткрывать. Заведите новую заявку
и добавьте ссылку на существующую. Если найденная заявка открыта, отпишитесь в нее.

Перед заведением заявки проверьте, что ошибка воспроизводится на последней актуальной версии приложения.
1. Используйте понятные формулировки.
1. Укажите версию приложения, на которой возникла проблема.
1. Опишите конкретные шаги, которые приводят к проблеме. Считается хорошим тоном, если предоставлено как можно больше
   подробностей. 
1. Опишите, что вы ожидали получить в результате своих действий и какой результат получен на самом деле.
1. Прикрепите сообщение об ошибке, которое отображена в браузере, прикрепите файл лога `portfolio.log`, конфигурационный
   файл `application-conf.properties`.

### Предложение об улучшении
Перед созданием предлжения проверьте текущие заявки, возможно улучшение уже предложено. В противном случае заведите
заявку и детально опишите ваши предложения.
1. Используйте понятные формулировки.
1. Укажите какую версию приложения вы используете.
1. Предоставьте пошаговое описание предлагаемого усовершенствования как можно подробнее.
1. Опишите текущее поведение и объясните, какое поведение вы ожидаете увидеть и почему.
1. Объясните, почему это усовершенствование будет полезно для большинства пользователей.

### Ваш первый вклад в качестве контрибьютора
Проблемы для новых контрибьюторов - проблемы, для которых требуется всего несколько строк кода.
Заявки, с которых стоит начинать отмечены лейблом
[good for first issue](#использование-лейблов).

### Добавление функциональности и исправление ошибок
Исправление ошибок и разработка новой функциональности должна вестись в pull request, дублировать разработку заявкой
не нужно. Однако заявка существует ее нужно прилинковать.

### Создание документации
Если вы считаете, что какая то часть работы приложения недостаточно задокументирована, создайте документацию и пулл реквест.
Документация располагается в директории [docs](./).

### Перевод документации
Согласно данным Московской биржи и НРД доля нерезидентов в ОФЗ достигает 30%, а на рынке акций - 50%.
Важным вкладом в развитие проекта является перевод существующей документации на другие языки.

## Использование лейблов
Работа с заявками (issues) и пулл реквестами (pull request) должна вестись с использованием лейблов (labels).

После заведения заявки или пулл реквеста, разработчики присваивают ей один из типов:
- `bug` - проблема;
- `feature` - новая функциональность;
- `improvement` - улучшение существующей функциональности;
- `doc` - запрос на доработку документации;
- `task` - задача, тип которой не может быть отнесен ни к одной из предыдущих категорий;

и приоритет:
- `critical` - для отчетов об ошибке, которые приводят к неработоспособности приложения во всех случаях
  или при определенных обстоятельствах;
- `major` - для существенных улучшений функциональности, которые затрагивают широкий круг пользователей,
  или для ошибок приложения (например, ошибка подсчета доходности, выплат дивидендов, купонов, амортизации);  
- `minor` - для улучшений функциональности, которые затрагивают узкий круг пользователей, или ошибок, не влияющих
  на основной функционал (например, опечатки).

После первичного анализа контрибьютор может выставить один из статусов:
- `question` - если имеется вопрос по заявке;
- `duplicate` - если заявка дублируют существующую, заявка связывается с существующей и закрывается;
- `invalid` - если заявка оформлена с ошибками, которые не могут быть сиправлены (например, вандализм).

Если работа может быть выполнена контрибьюторами, заявка помечается одним из лейблов:
- `good first issue`;
- `help wanted`.

В процессе работы заявки изменяется ее статус:
- _нет статуса_ - для новых заявок;
- `planned` - объем доработок определен и запланирован;
- `wontfix` - ошибки в работе приложения не найдено ил в процессе обсуждения решено отклонить новую функциональность;
- `in progress` - заявка в работе;
- `done` - заявка выполнена, ожидает ревью кода или мерджа в релизную ветку или в ветку фикса.

Работа с репозиторием ведется в рамках следующих [соглашений](https://habr.com/ru/post/106912/).