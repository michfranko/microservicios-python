from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
import requests

from database import SessionLocal, engine
from models import Order, Base

USERS_SERVICE_URL = "http://users_service:8001"

app = FastAPI(title="Orders Service")

Base.metadata.create_all(bind=engine)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/orders/")
def create_order(product: str, user_id: int, db: Session = Depends(get_db)):
    order = Order(product=product, user_id=user_id)
    db.add(order)
    db.commit()
    db.refresh(order)
    return order

@app.get("/orders/{order_id}")
def get_order(order_id: int, db: Session = Depends(get_db)):
    order = db.query(Order).filter(Order.id == order_id).first()
    if not order:
        return {"error": "Pedido no encontrado"}

    user = requests.get(
        f"{USERS_SERVICE_URL}/users/{order.user_id}"
    ).json()

    return {
        "order": order,
        "user": user
    }
