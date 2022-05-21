### Orders

#### GET /api/orders

Response:

```json
[
  {
  "id" : 3,
  "dateOrdered" : 1639198835000,
  "comments" : null,
  "tableNum" : 11,
  "status" : "FINISHED", // NEW, IN_PROGRESS, READY, FINISHED, PAID
  "details" : [ {
    "id" : 187,
    "dishId" : 682,
    "orderId" : 3,
    "quantity" : 1,
    "comment" : "resuscitation's,substitution's",
    "status" : "FINISHED",
    "cost" : 32.0,
    "statusDesc" : "Выполнено"
  } ],
  "statusDescription" : "Выполнено",
  "cost" : 32.0,
  "dishNames" : "Блюдо 682 (1 шт.)"
 }
]
```

#### GET /api/order?id=<id>

Response:

```json
{
  "id" : 3,
  "dateOrdered" : 1639198835000,
  "comments" : null,
  "tableNum" : 11,
  "status" : "FINISHED",
  "details" : [ {
    "id" : 187,
    "dishId" : 682,
    "orderId" : 3,
    "quantity" : 1,
    "comment" : "resuscitation's,substitution's",
    "status" : "FINISHED",
    "cost" : 32.0,
    "statusDesc" : "Выполнено"
  } ],
  "statusDescription" : "Выполнено",
  "cost" : 32.0,
  "dishNames" : "Блюдо 682 (1 шт.)"
}
```

#### POST /api/order

Payload:

```json
{
  "tableNum": 1,
  "comments": "Some comment",
  "details": [
    {
      "dish_id": 1,
      "quantity": 1
    }
  ]
}
```

Response:

```json
{
  "id": 1,
  "dateOrdered": 1334453,
  "comments": "Some comment",
  "tableNum": 1,
  "status": "NEW",
  "details": [
    {
      "id": 1,
      "order_id": 1,
      "dish_id": 1,
      "quantity": 1,
      "status": "NEW"
    }
  ]
}
```

#### PUT /api/order

Payload:

```json
{
  "id": 1,
  "tableNum": 1,
  "comments": "Some comment",
  "details": [
    {
      "dish_id": 1,
      "quantity": 1,
      "status": "NEW"
    }
  ]
}
```

Response:

```json
{
  "id": 1,
  "dateOrdered": 1334453,
  "comments": "Some comment",
  "tableNum": 1,
  "status": "NEW",
  "details": [
    {
      "id": 1,
      "order_id": 1,
      "dish_id": 1,
      "quantity": 1,
      "status": "NEW"
    }
  ]
}
```

#### DELETE /api/order?id=`id`

Status 200 - No response

Status 404 - {"No order found with id `id`"}

### Categories

#### GET /api/categories

Response:

```json
[
  {
    "id": 1,
    "name": "Some category",
    "categoryOrder": 1
  }
]
```

#### GET /api/category?id=`id`

Response:

```json
{
  "id": 1,
  "name": "Some category",
  "categoryOrder": 1
}
```

#### POST /api/category

Payload:

```json
{
  "name": "Some category",
  "categoryOrder": 1
}
```

Status 200 Response:

```json
{
  "id": 12,
  "name": "Some category",
  "categoryOrder": 1
}
```

Status 406 Response:

```json
{
  "name": [
    "Название категории должно содержать только буквы (латинские или русские) и быть в пределах 2-200 символов."
  ]
}
```

#### PUT /api/category?id=`id`

Payload:

```json
{
  "id": 12,
  "name": "Some other category",
  "categoryOrder": 2
}
```

Status 200 Response:

```json
{
  "id": 12,
  "name": "Some other category",
  "categoryOrder": 2
}
```

Status 406 Response:

```json
{
  "name": [
    "Название категории должно содержать только буквы (латинские или русские) и быть в пределах 2-200 символов."
  ]
}
```

#### DELETE /api/order?id=`id`

Status 200 - No response

Status 404 - {"No category found by given id of `id`"}

### Dishes

GET /api/dishes

Response:

```json
[
  {
    "id": 1,
    "categoryId": 1,
    "name": "Some dish",
    "weight": 100.0,
    "price": 100.0,
    "ingredients": "ingr 1, ingr 2...",
    "available": true,
    "imagePath": "DishImages/test2_-_17.01.2022_14.13.26.jpg"
  }
]
```

#### GET /api/dishes_th `//dishes with thumbnails 20x20`

Response:

```json
[
  {
    "id": 1,
    "categoryId": 1,
    "name": "Some dish",
    "weight": 100.0,
    "price": 100.0,
    "ingredients": "ingr 1, ingr 2...",
    "available": true,
    "imagePath": "DishImages/thumbnails/test2_-_17.01.2022_14.13.26.jpg"
  }
]
```

#### GET /api/dish?id=`id`

Response:

```json
{
    "id": 1,
    "categoryId": 1,
    "name": "Some dish",
    "weight": 100.0,
    "price": 100.0,
    "ingredients": "ingr 1, ingr 2...",
    "available": true,
    "imagePath": "DishImages/test2_-_17.01.2022_14.13.26.jpg"
  }
```

#### POST /api/dish

Payload:

`//?` = NOT mandatory

```json
{
    "categoryId": 1,
    "name": "Some dish", //not blank
    "weight": 100.0,  // >=1
    "price": 100.0,   //? 
    "ingredients": "ingr 1, ingr 2...", //?
    "available": true //?
  }
```

Status 200 Response:

```json
{
    "id": 1,
    "categoryId": 1,
    "name": "Some dish",
    "weight": 100.0,
    "price": 100.0,
    "ingredients": "ingr 1, ingr 2...",
    "available": true,
    "imagePath": "DishImages/test2_-_17.01.2022_14.13.26.jpg"
  }
```

Status 406 Response:

```json
{
  "name": [
    "Error message"
  ]
}
```

#### PUT /api/dish?id=<id>

Payload:

`//?` = NOT mandatory

```json
{
    "id":1,
    "categoryId": 1,
    "name": "Some dish", //not blank
    "weight": 100.0,  // >=1
    "price": 100.0,   //? 
    "ingredients": "ingr 1, ingr 2...", //?
    "available": true //?
  }
```

Status 200 Response:

```json
{
    "id": 1,
    "categoryId": 1,
    "name": "Some dish",
    "weight": 100.0,
    "price": 100.0,
    "ingredients": "ingr 1, ingr 2...",
    "available": true,
    "imagePath": "DishImages/test2_-_17.01.2022_14.13.26.jpg"
  }
```

Status 406 Response:

```json
{
  "name": [
    "Error message"
  ]
}
```

#### DELETE /api/order?id=`id`

Status 200 - No response

Status 404 - `{"No dish was found by given id of <id>"}`

### Details

GET /api/detail?id=`3`
Status 200 Response:

```json
{
  "id" : 3,
  "dishId" : 623,
  "orderId" : 1563,
  "quantity" : 3,
  "comment" : "bulks,bend",
  "status" : "IN_PROGRESS",
  "cost" : 84.0,
  "statusDesc" : "Готовится"
}
```

Status 404 Response:
`No detail found for given id.`

#### POST /api/detail

Payload:

```json
{
  "orderId": 1000,
  "dishId": 1000,
  "quantity": 1
}
```

Status 200 Response:

```json
{
    "id": 3005,
    "dishId": 1000,
    "orderId": 1000,
    "quantity": 1,
    "comment": null,
    "status": "NEW",
    "cost": 46.0,
    "statusDesc": "Новый"
}
```

Status 406 Response:

```json
{
    "orderId": [
        "должно быть больше или равно 1"
    ],
    "dishId": [
        "должно быть больше или равно 1"
    ]
}
```

#### POST /api/detail/status?id=`3004`&status=`READY`

##### Statuses: `NEW`, `IN_PROGRESS`, `READY`, `FINISHED`, `PAID`

Status 200 Response:
`Status was updated successfully`

Status 404 Response:
`No detail found by given id`

#### PUT /api/detail

Payload:

```json
{
  "id": 3004,
  "dishId": 10,
  "orderId": 10,
  "quantity": 1,
  "comment": null,
  "status": "NEW",
  "cost": 46.0,
  "statusDesc": "Новый"
}
```

Status 200 Response:

```json
{
  "id": 3004,
  "dishId": 10,
  "orderId": 10,
  "quantity": 1,
  "comment": null,
  "status": "NEW",
  "cost": 60.0,
  "statusDesc": "Новый"
}
```

#### DELETE /api/detail

Response: Status 200