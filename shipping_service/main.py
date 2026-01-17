from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import logging

logging.basicConfig(level=logging.INFO)

app = FastAPI(title="Shipping Calculator Service")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.middleware("http")
async def log_requests(request: Request, call_next):
    if request.method == "POST" and request.url.path == "/shipping/calculate":
        body = await request.body()
        logging.info(f"Raw request body: {body}")
    response = await call_next(request)
    return response

class ShippingRequest(BaseModel):
    product_id: int
    destination: str

@app.post("/shipping/calculate")
async def calculate_shipping(request: Request, data: ShippingRequest):
    logging.info(f"Parsed request: product_id={data.product_id}, destination={data.destination}")
    # Base cost per product
    base_cost_per_product = 5.0
    
    # Additional cost based on destination
    destination_costs = {
        "usa": 10.0,
        "canada": 15.0,
        "brazil": 25.0,
        "mexico": 12.0,
        "argentina": 20.0,
        "colombia": 18.0,
        "peru": 16.0,
        "chile": 22.0,
        "ecuador": 14.0,
        "bolivia": 19.0,
        "paraguay": 17.0,
        "uruguay": 21.0,
        "venezuela": 23.0,
        "guyana": 26.0,
        "suriname": 24.0,
        "french guiana": 27.0
    }
    
    destination_lower = request.destination.lower()
    additional_cost = destination_costs.get(destination_lower, 10.0)  # Default to 10 if not found
    
    # Total cost: base per product + additional for destination
    cost = base_cost_per_product + additional_cost
    return {"cost": cost}