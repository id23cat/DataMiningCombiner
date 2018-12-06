# Data Mining Combiner HATEOAS REST API

## Account

### Пример Структуры Account DTO
```
> {
>    "accountId":1,
>    "userName":"devel",
>    "email":"devel@mail.org",
>    "firstName":"Devel",
>    "lastName":"Oper",
>    "created":null,
>    "role":"USER",
>    "dtoId":1
>    "_links": {
>       "self": {"href":"http://localhost:8081/rest/user/1"},
>       "getAccount": {"href":"http://localhost:8081/rest/user/1","type":"GET"},
>       "updateAccount": {"href":"http://localhost:8081/rest/user/1","type":"PUT"},
>       "addAccount": { "href":"http://localhost:8081/rest/user","type":"POST"},
>       "deleteAccount":{"href":"http://localhost:8081/rest/user/1","type":"DELETE"},
>       "getAccountList": {"href":"http://localhost:8081/rest/user/all","type":"GET"},
>       "getProjectList":{"href":"http://localhost:8081/rest/1/project/all","type":"GET"}
>    }
> }
```
Пример запроса на сервер c целью получения аккаунта с id = 1:
```
http://localhost:8081/rest/user/1
```

Пример ответа на вышеуказанный запрос можно увидеть в структуре Account DTO.

Далее детально рассмотрим структуру ответа.
### Данные об аккаунте
```
accountId":1,"userName":"devel","email":"devel@mail.org","firstName":"Devel","lastName":"Oper","created":null,"role":"USER","dtoId":1
```
### Ссылки
- На сам запрос (selfLink):
```
"self":{"href":"http://localhost:8081/rest/user/1"}
```
- На другие запросы связанные с запрашиваемой сущностью
```
"_links":{"self":{"href":"http://localhost:8081/rest/user/1"},"addAccount":{"href":"http://localhost:8081/rest/user","type":"POST"},"getAccount":{"href":"http://localhost:8081/rest/user/1","type":"GET"},"getAccountList":{"href":"http://localhost:8081/rest/user/all","type":"GET"},"updateAccount":{"href":"http://localhost:8081/rest/user","type":"PUT"},"deleteAccount":{"href":"http://localhost:8081/rest/user/1","type":"GET"},"getProjectList":{"href":"http://localhost:8081/rest/1/project/all","type":"GET"}}
```
| Имя ссылки | Типо HTTP запроса | Ссылка | Описание | Примечание |
| ------ | ------ | ------ | ------ | ------ |
| getAccount | GET | "href":"http://localhost:8081/rest/user/1" | возвращает аккаунт по ID | - |
| updateAccount | PUT | "href":"http://localhost:8081/rest/user" | редактирует существующий аккаунт | тело запроса содержит Account DTO |
| addAccount | POST | "href":"http://localhost:8081/rest/user" | добавляет новый аккаунт | тело запроса содержит Account DTO |
| deleteAccount | DELETE | "href":"http://localhost:8081/rest/user/1" | удаляет аккаунт по ID | - |
| getAccountList | GET | "href":"http://localhost:8081/rest/user/all" | возвращает все аккаунты | - |
| getProjectList | GET | "href":"http://localhost:8081/rest/1/project/all" | возвращает все проекты опреденного аккаунта | - |

- На запросы связанные с дочерними сущностями

| Имя ссылки | Типо HTTP запроса | Ссылка | Описание | Примечание |
| ------ | ------ | ------ | ------ | ------ |
| getProjectList | GET | "href":"http://localhost:8081/rest/1/project/all" | возвращает все проекты опреденного аккаунта | - |

## Project

### Пример Структуры Project DTO
```
> {
    "projectId":1,
    "projectType":"SIMPLEST_PROJECT"
    ,"name":"proj0",
    "created":"2018-11-18T18:20:31.765+0000",
    "dtoId":1,
    "_links":{
        "self":{"href":"http://localhost:8081/rest/1/project/1"},
        "addProject":{"href":"http://localhost:8081/rest/1/project","type":"POST"},
        "getProjectList":{"href":"http://localhost:8081/rest/1/project/all","type":"GET"},
        "updateProject":{"href":"http://localhost:8081/rest/1/project","type":"PUT"},
        "deleteProject":{"href":"http://localhost:8081/rest/1/project/1","type":"DELETE"},
        "getProjectList":{"href":"http://localhost:8081/rest/1/project/all","type":"GET"},
        "getDatasetList":{"href":"http://localhost:8081/rest/1/project/1/dataset/all","type":"GET"}
    }
}
```
Пример запроса на сервер c целью получения проекта с id = 1, где id aккаунта = 1:
```
http://localhost:8081/rest/1/project/1
```

Пример ответа на вышеуказанный запрос можно увидеть в структуре Project DTO.

Далее детально рассмотрим структуру ответа.
### Данные о проекте
```
"projectId":1,"projectType":"SIMPLEST_PROJECT","name":"proj0","created":"2018-11-18T18:20:31.765+0000","dtoId":1
```
### Ссылки
- На сам запрос (self link):
```
"self":{"href":"http://localhost:8081/rest/1/project/1"}
```
- На другие запросы связанные с запрашиваемой сущностью
```
"_links":{
    "self":{"href":"http://localhost:8081/rest/1/project/1","type":"GET"},
    "addProject":{"href":"http://localhost:8081/rest/1/project","type":"POST"},
    "getProjectList":{"href":"http://localhost:8081/rest/1/project/all","type":"GET"},
    "updateProject":{"href":"http://localhost:8081/rest/1/project","type":"PUT"},
    "deleteProject":{"href":"http://localhost:8081/rest/1/project/1","type":"DELETE"},
    "getProjectList":{"href":"http://localhost:8081/rest/1/project/all","type":"GET"},
    "getDatasetList":{"href":"http://localhost:8081/rest/1/project/1/dataset/all","type":"GET"}
}
```
| Имя ссылки | Типо HTTP запроса | Ссылка | Описание | Примечание |
| ------ | ------ | ------ | ------ | ------ |
| getProject | GET | "href":"http://localhost:8081/rest/1/project/1" | возвращает проект по ID у соответсвующего аккаунта | - |
| updateProject | PUT | "href":"http://localhost:8081/rest/1/project" | редактирует существующий проект | тело запроса содержит Project DTO |
| addProject | POST | "href":"http://localhost:8081/rest/1/project" | добавляет новый проект к аккаунту | тело запроса содержит Project DTO |
| deleteProject | DELETE | "href":"http://localhost:8081/rest/1/project/1" | удаляет проект по ID | - |
| getProjectList | GET | "href":"http://localhost:8081/rest/1/project/all" | возвращает все проекты опреденного аккаунта | - |

- На запросы связанные с дочерними сущностями

| Имя ссылки | Типо HTTP запроса | Ссылка | Описание | Примечание |
| ------ | ------ | ------ | ------ | ------ |
| getDatasetList | GET | "href":"http://localhost:8081/rest/1/project/1/dataset/all" | возвращает все наборы данных проекта у опреденного аккаунта | - |

## Data Set

### Пример Структуры Data Set DTO
```
{
    "metaDataId":1,
    "name":"telecom",
    "description":"",
    "attributes":{
        "Total intl minutes":{
            "name":"Total intl minutes",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total day charge":{
            "name":"Total day charge",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total day calls":{
            "name":"Total day calls",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Number vmail messages":{
            "name":"Number vmail messages",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total night charge":{
            "name":"Total night charge",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Churn":{
            "name":"Churn",
            "type":"NOMINAL",
            "multiplier":1.0,
            "checked":true
        },
        "Total night minutes":{
            "name":"Total night minutes",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total eve calls":{
            "name":"Total eve calls",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total day minutes":{
            "name":"Total day minutes",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Customer service calls":{
            "name":"Customer service calls",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total intl charge":{
            "name":"Total intl charge",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Area code":{
            "name":"Area code",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total night calls":{
            "name":"Total night calls",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "International plan":{
            "name":"International plan",
            "type":"NOMINAL",
            "multiplier":1.0,
            "checked":true
        },
        "State":{
        "name":"State",
        "type":"NOMINAL",
        "multiplier":1.0,
        "checked":true
        },
        "Account length":{
            "name":"Account length",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total intl calls":{
            "name":"Total intl calls",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total eve minutes":{
            "name":"Total eve minutes",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Total eve charge":{
            "name":"Total eve charge",
            "type":"NUMERIC",
            "multiplier":1.0,
            "checked":true
        },
        "Voice mail plan":{
            "name":"Voice mail plan",
            "type":"NOMINAL",
            "multiplier":1.0,
            "checked":true
        }
    },
    "dtoId":1,
    "_links":{
        "self":{"href":"http://localhost:8081/rest/1/project/1/dataset/1"},
        "getDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset/1","type":"GET"},
        "addDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset","type":"POST"},
        "getDatasetList":{"href":"http://localhost:8081/rest/1/project/1/dataset/all","type":"GET"},
        "updateDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset","type":"PUT"},
        "deleteDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset/1","type":"DELETE"}
    }
}
```
Пример запроса на сервер c целью получения набора данных с id = 1, где id проекта = 1, id aккаунта = 1:
```
"href":"http://localhost:8081/rest/1/project/1/dataset/1
```

Пример ответа на вышеуказанный запрос можно увидеть в структуре Data Set DTO.

Далее детально рассмотрим структуру ответа.

### Ссылки
- На сам запрос (self link):
```
"self":{"href":"http://localhost:8081/rest/1/project/1"}
```
- На другие запросы связанные с запрашиваемой сущностью
```
"_links":{
    "self":{"href":"http://localhost:8081/rest/1/project/1/dataset/1","type":"GET"},
    "getDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset/1","type":"GET"},
    "addDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset","type":"POST"},
    "getDatasetList":{"href":"http://localhost:8081/rest/1/project/1/dataset/all","type":"GET"},
    "updateDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset","type":"PUT"},
    "deleteDataset":{"href":"http://localhost:8081/rest/1/project/1/dataset/1","type":"DELETE"}
}
```
| Имя ссылки | Типо HTTP запроса | Ссылка | Описание | Примечание |
| ------ | ------ | ------ | ------ | ------ |
| getDataset | GET | "href":"http://localhost:8081/rest/1/project/1/dataset/1" | возвращает набор данных по ID c определенным проектом по ID у соответсвующего аккаунта | - |
| updateDataset | PUT | "href":"http://localhost:8081/rest/1/project/1/dataset" | редактирует существующий набор данных | тело запроса содержит Data Set DTO |
| addDataset | POST | "href":"http://localhost:8081/rest/1/project/1/dataset" | добавляет новый набор данных к проекту у аккаунта | тело запроса содержит Data Set DTO |
| deleteDataset | DELETE | "href":"http://localhost:8081/rest/1/project/1/dataset/1" | удаляет набор данных по ID | - |
| getDatasetList | GET | "href":"http://localhost:8081/rest/1/project/1/dataset/all" | возвращает все наборы данных проекта у опреденного аккаунта | - |

## Algorithm

### Пример Структуры Algorithm DTO
```
{
    "algorithmId":1,
    "name":null,
    "description":null,
    "project":null,
    "dataSource":null,
    "srcAttributes":null,
    "dataDestination":null,
    "method":null,
    "dtoId":1,
    "_links":{
        "self":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1"},
        "addAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm","type":"POST"},
        "getAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1","type":"GET"},
        "getAlgorithmList":{"href":"http://localhost:8081/rest/1/project/1/algorithm/all","type":"GET"},
        "updateAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm","type":"PUT"},
        "deleteAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1","type":"DELETE"}
    }
}
```
Пример запроса на сервер c целью получения набора данных с id = 1, где id проекта = 1, id aккаунта = 1:
```
http://localhost:8081/rest/1/project/1/algorithm/1
```

Пример ответа на вышеуказанный запрос можно увидеть в структуре Algorithm DTO.

Далее детально рассмотрим структуру ответа.

### Ссылки
- На сам запрос (self link):
```
"self":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1"}
```
- На другие запросы связанные с запрашиваемой сущностью
```
"_links":{
    "self":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1"},
    "addAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm","type":"POST"},
    "getAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1","type":"GET"},
    "getAlgorithmList":{"href":"http://localhost:8081/rest/1/project/1/algorithm/all","type":"GET"},
    "updateAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm","type":"PUT"},
    "deleteAlgorithm":{"href":"http://localhost:8081/rest/1/project/1/algorithm/1","type":"DELETE"}
}
```
| Имя ссылки | Типо HTTP запроса | Ссылка | Описание | Примечание |
| ------ | ------ | ------ | ------ | ------ |
| getAlgorithm | GET | "href":"http://localhost:8081/rest/1/project/1/algorithm/1" | возвращает алгоритм по ID c определенным проектом по ID у соответсвующего аккаунта | - |
| updateAlgorithm | PUT | "href":"http://localhost:8081/rest/1/project/1/algorithm" | редактирует существующий алгоритм | тело запроса содержит Data Set DTO |
| addAlgorithm | POST | "href":"http://localhost:8081/rest/1/project/1/algorithm" | добавляет новый алгоритм к проекту у аккаунта | тело запроса содержит Data Set DTO |
| deleteAlgorithm | DELETE | "href":"http://localhost:8081/rest/1/project/1/algorithm/1" | удаляет алгоритм по ID | - |
| getAlgorithmList | GET | "href":"http://localhost:8081/rest/1/project/1/algorithm/all" | возвращает все алгоритмы проекта у опреденного аккаунта | - |
