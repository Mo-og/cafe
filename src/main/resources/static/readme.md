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
    status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
    details: [
        {
            id: 1,
            order_id: 1,
            dish_id: 1,
            quantity: 1,
            status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
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
            status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
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
    status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
    details: [
        {
            id: 1,
            order_id: 1,
            dish_id: 1,
            quantity: 1,
            status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
        }
    ]
}
```

DELETE /api/order?id=<id>

No response

**Categories**

GET /api/categories

Response:

```
[
  {
    id: 1,
    name: 'Some category',
    //for sorting categories
    categoryOrder: 1,
  }
]
```

GET /api/category?id=<id>

Response:

```
{
  id: 1,
  name: 'Some category',
  categoryOrder: 1,
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
    status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
    details: [
        {
            id: 1,
            order_id: 1,
            dish_id: 1,
            quantity: 1,
            status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
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
            status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
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
    status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
    details: [
        {
            id: 1,
            order_id: 1,
            dish_id: 1,
            quantity: 1,
            status: 'NEW',// NEW, IN_PROGRESS, READY, FINISHED
        }
    ]
}
```

DELETE /api/order?id=<id>

No response
