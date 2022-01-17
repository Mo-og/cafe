**Orders**

GET /api/orders

Response:

```
[
  {
    id: 1,
    dateOrdered: 1334453,
    comments: 'Some comment',
    tableNum: 1,
    status: 'NEW',
    // NEW, IN_PROGRESS, READY, FINISHED
    details: [
      {
        id: 1,
        order_id: 1,
        dish_id: 1,
        quantity: 1,
        status: 'NEW',
        // NEW, IN_PROGRESS, READY, FINISHED
      }
    ]
  }
]
```

GET /api/order?id=<id>

Response:

```
{
  id: 1,
  dateOrdered: 1334453,
  comments: 'Some comment',
  tableNum: 1,
  status: 'NEW',
  // NEW, IN_PROGRESS, READY, FINISHED
  details: [
    {
      id: 1,
      order_id: 1,
      dish_id: 1,
      quantity: 1,
      status: 'NEW',
      // NEW, IN_PROGRESS, READY, FINISHED
    }
  ]
}
```

POST /api/order

Payload:

```
{
  tableNum: 1,
  comments: 'Some comment',
  details: [
    {
      dish_id: 1,
      quantity: 1
    }
  ]
}
```

Response:

```
{
  id: 1,
  dateOrdered: 1334453,
  comments: 'Some comment',
  tableNum: 1,
  status: 'NEW',
  // NEW, IN_PROGRESS, READY, FINISHED
  details: [
    {
      id: 1,
      order_id: 1,
      dish_id: 1,
      quantity: 1,
      status: 'NEW',
      // NEW, IN_PROGRESS, READY, FINISHED
    }
  ]
}
```

PUT /api/order

Payload:

```
{
  id: 1,
  tableNum: 1,
  comments: 'Some comment',
  details: [
    {
      dish_id: 1,
      quantity: 1,
      status: 'NEW',
      // NEW, IN_PROGRESS, READY, FINISHED
    }
  ]
}
```

Response:

```
{
  id: 1,
  dateOrdered: 1334453,
  comments: 'Some comment',
  tableNum: 1,
  status: 'NEW',
  // NEW, IN_PROGRESS, READY, FINISHED
  details: [
    {
      id: 1,
      order_id: 1,
      dish_id: 1,
      quantity: 1,
      status: 'NEW',
      // NEW, IN_PROGRESS, READY, FINISHED
    }
  ]
}
```

DELETE /api/order?id=<id>

Status 200 - No response

Status 404 - {"No order found with id <id>"}

**Categories**

GET /api/categories

Response:

```
[
  {
    id: 1,
    name: 'Some category',
    //for sorting categories
    categoryOrder: 1
  }
]
```

GET /api/category?id=<id>

Response:

```
{
  id: 1,
  name: 'Some category',
  categoryOrder: 1
}
```

POST /api/category

Payload:

```
{
  name: 'Some category',
  categoryOrder: 1
}
```

Status 200 Response:

```
{
  id: 12,
  name: 'Some category',
  categoryOrder: 1
}
```

Status 406 Response:

```
{
  "name": [
    "Название категории должно содержать только буквы (латинские или русские) и быть в пределах 2-200 символов."
  ]
}
```

PUT /api/category?id=<id>

Payload:

```
{
  id: 12,
  name: 'Some other category',
  categoryOrder: 2
}
```

Status 200 Response:

```
{
  id: 12,
  name: 'Some other category',
  categoryOrder: 2
}
```

Status 406 Response:

```
{
  "name": [
    "Название категории должно содержать только буквы (латинские или русские) и быть в пределах 2-200 символов."
  ]
}
```

DELETE /api/order?id=<id>

Status 200 - No response

Status 404 - {"No category found by given id of <id>"}

**Dishes**

GET /api/dishes

Response:

```
[
  {
    id: 1,
    categoryId: 1,
    name: 'Some dish',
    weight: 100.0,
    price: 100.0,
    ingredients: "ingr 1, ingr 2...",
    available: true,
    status: 'NEW',
    // IN_PROGRESS, READY, FINISHED; (enum)
    imagePath: 'DishImages/test2_-_17.01.2022_14.13.26.jpg'
  }
]
```

GET /api/dishes_th //dishes with thumbnails 20x20

Response:

```
[
  {
    id: 1,
    categoryId: 1,
    name: 'Some dish',
    weight: 100.0,
    price: 100.0,
    ingredients: "ingr 1, ingr 2...",
    available: true,
    status: 'NEW',
    // IN_PROGRESS, READY, FINISHED; (enum)
    imagePath: 'DishImages/thumbnails/test2_-_17.01.2022_14.13.26.jpg'
  }
]
```

GET /api/dish?id=<id>

Response:

```
  {
    id: 1,
    categoryId: 1,
    name: 'Some dish',
    weight: 100.0,
    price: 100.0,
    ingredients: "ingr 1, ingr 2...",
    available: true,
    status: 'NEW',// IN_PROGRESS, READY, FINISHED; (enum)
    imagePath: 'DishImages/test2_-_17.01.2022_14.13.26.jpg'
  }
```

POST /api/dish

Payload:

//? - NOT mandatory

```
  {
    categoryId: 1,
    name: 'Some dish', //not blank
    weight: 100.0,  // >=1
    price: 100.0,   //? 
    ingredients: "ingr 1, ingr 2...", //?
    available: true, //?
    status: 'NEW',// IN_PROGRESS, READY, FINISHED; (enum) //?
  }
```

Status 200 Response:

```
  {
    id: 1,
    categoryId: 1,
    name: 'Some dish',
    weight: 100.0,
    price: 100.0,
    ingredients: "ingr 1, ingr 2...",
    available: true,
    status: 'NEW',// IN_PROGRESS, READY, FINISHED; (enum)
    imagePath: 'DishImages/test2_-_17.01.2022_14.13.26.jpg'
  }
```

Status 406 Response:

```
{
  "name": [
    "Error message"
  ]
}
```

PUT /api/dish?id=<id>

Payload:

```
  {
    id:1,
    categoryId: 1,
    name: 'Some dish', //not blank
    weight: 100.0,  // >=1
    price: 100.0,   //? 
    ingredients: "ingr 1, ingr 2...", //?
    available: true, //?
    status: 'NEW',// IN_PROGRESS, READY, FINISHED; (enum) //?
  }
```

Status 200 Response:

```
  {
    id: 1,
    categoryId: 1,
    name: 'Some dish',
    weight: 100.0,
    price: 100.0,
    ingredients: "ingr 1, ingr 2...",
    available: true,
    status: 'NEW',// IN_PROGRESS, READY, FINISHED; (enum)
    imagePath: 'DishImages/test2_-_17.01.2022_14.13.26.jpg'
  }
```

Status 406 Response:

```
{
  "name": [
    "Error message"
  ]
}
```

DELETE /api/order?id=<id>

Status 200 - No response

Status 404 - {"No dish was found by given id of <id>"}
