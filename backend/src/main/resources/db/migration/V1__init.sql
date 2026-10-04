CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  price NUMERIC(10,2) NOT NULL,
  stock INT NOT NULL
);
CREATE TABLE orders (
  id BIGSERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL REFERENCES products(id),
  quantity INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT now()
);
INSERT INTO products(name, price, stock) VALUES
  ('Mechanical Keyboard', 79.99, 50), ('USB-C Hub', 29.50, 100), ('27in Monitor', 249.00, 20);
