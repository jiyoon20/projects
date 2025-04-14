
# Cafe Management API Documentation

> Base URL: `http://localhost:8080`  
> Version: `v0`

---

## Tags

- **User**: User registration and login
- **Admin**: Admin-exclusive operations
- **Menu**: Menu creation, modification, and deletion
- **Cart**: Cart operations for users
- **Order**: Placing and managing orders

---

## User APIs

### Sign Up

**GET** `/user/signup`  
> Displays the user signup form.

**POST** `/user/signup`  
> Registers a new user.  
**Request Body:**
```json
{
  "username": "string",
  "password1": "string",
  "password2": "string",
  "email": "string"
}
```

---

### Login

**GET** `/user/login`  
> Displays the login form.

---

## Menu APIs

### Create Menu (Admin)

**GET** `/menu/create`  
> Shows the menu creation form.

**POST** `/menu/create?selectedImage=example.jpg`  
> Creates a new menu item.  
**Request Body:**
```json
{
  "title": "string",
  "price": 0,
  "content": "string",
  "coffeeImageUrl": "string",
  "calories": 0
}
```

---

### Modify Menu (Admin)

**GET** `/menu/modify/{menuId}`  
**POST** `/menu/modify/{menuId}`  
> Modify existing menu by ID.

---

### Menu Detail

**GET** `/menu/detail/{menuId}`  
> Displays a specific menu item.

### Delete Menu (Admin)

**GET** `/menu/delete/{menuId}`  
> Deletes the specified menu item.

---

### List Menu

**GET** `/menu/list`  
> Shows all menu items.

---

## Cart APIs

### Add to Cart

**POST** `/cart/add?menuId={id}`  
> Adds a menu item to the cart.

---

### Update Cart

**POST** `/cart/update?menuId={id}&action={increase|decrease}`  
> Updates item quantity in the cart.

---

### View Cart

**GET** `/cart/view`  
> Shows contents of the user's cart.

---

##  Order APIs

###  Submit Order

**POST** `/order/submit`  
> Places the current order.

###  Review Order

**GET** `/order/submit`  
> Shows submitted order details.

---

###  Admin: View All Orders

**GET** `/admin/order/list?page=0`  
> Lists all customer orders (paginated).

---

### Admin: Update Order Status

**POST** `/admin/order/status?orderId={id}&orderStatus={PENDING|ORDERED|PROCESSING|COMPLETED|CANCELLED}`

---

##  Admin APIs

###  Sign Up

**GET** `/admin/signup`  
**POST** `/admin/signup`  
> Registers a new admin.  
**Request Body:**
```json
{
  "username": "string",
  "password1": "string",
  "password2": "string",
  "email": "string"
}
```

---

### Dashboard / Entry

**GET** `/admin/`  
> Admin landing page  
**GET** `/admin/main`  
> Dashboard page

---

###  Manage Customers

**GET** `/admin/customers`  
> Lists all registered customers.  
**POST** `/admin/customers/{userId}/toggle-active`  
> Activate/deactivate a customer.

---

###  Upload Menu Image (S3)

**POST** `/admin/upload/images`  
> Uploads an image and returns S3 URL.
