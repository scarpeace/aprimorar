import uuid
import random
from datetime import datetime, timedelta

def random_date(start_age, end_age):
    end_date = datetime.now() - timedelta(days=start_age*365)
    start_date = datetime.now() - timedelta(days=end_age*365)
    random_days = random.randrange((end_date - start_date).days)
    return (start_date + timedelta(days=random_days)).strftime('%Y-%m-%d')

first_names = ["Ana", "Bruno", "Carlos", "Daniela", "Eduardo", "Fernanda", "Gabriel", "Helena", "Igor", "Julia", "Lucas", "Mariana", "Nicolas", "Olivia", "Pedro", "Quintino", "Rafael", "Sofia", "Tiago", "Ursula", "Vitor", "Vitoria", "Wagner", "Xuxa", "Yuri", "Zelia", "Alice", "Miguel", "Arthur", "Laura"]
last_names = ["Silva", "Santos", "Oliveira", "Souza", "Rodrigues", "Ferreira", "Alves", "Pereira", "Lima", "Gomes", "Costa", "Ribeiro", "Martins", "Carvalho", "Almeida", "Lopes", "Soares", "Fernandes", "Vieira", "Barbosa"]

def generate_name():
    return f"{random.choice(first_names)} {random.choice(last_names)}"

with open('src/main/resources/data.sql', 'w') as f:
    f.write("-- tb_employees\n")
    duties = ['TEACHER']*15 + ['ADM']*1 + ['SYSTEM']*1 + ['THERAPIST']*2 + ['MENTOR']*1
    
    for i in range(20):
        emp_id = uuid.uuid4()
        name = "Gustavo Scarpellini" if duties[i] == 'SYSTEM' else generate_name()
        birthdate = random_date(22, 50) if duties[i] != 'SYSTEM' else '1990-01-01'
        cpf = f"{random.randint(100000000, 999999999)}{i:02d}"
        contact = f"1199999{i:04d}"
        email = f"emp{i}@test.com" if duties[i] != 'SYSTEM' else "gustavo@test.com"
        pix = contact
        duty = duties[i]
        
        f.write(f"INSERT INTO tb_employees (id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at, duty) VALUES ('{emp_id}', '{name}', '{birthdate}', '{cpf}', '{contact}', '{email}', '{pix}', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{duty}');\n")

    f.write("\n-- tb_parent\n")
    parent_ids = [uuid.uuid4() for _ in range(15)]
    for i in range(15):
        pid = parent_ids[i]
        name = generate_name()
        birthdate = random_date(35, 50)
        cpf = f"{random.randint(100000000, 999999999)}{i+20:02d}"
        contact = f"1188888{i:04d}"
        email = f"parent{i}@test.com"
        pix = contact
        
        f.write(f"INSERT INTO tb_parent (id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at) VALUES ('{pid}', '{name}', '{birthdate}', '{cpf}', '{contact}', '{email}', '{pix}', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);\n")

    f.write("\n-- tb_students\n")
    student_ids = [uuid.uuid4() for _ in range(30)]
    for i in range(30):
        sid = student_ids[i]
        pid = parent_ids[i // 2]
        name = generate_name()
        birthdate = random_date(10, 18)
        cpf = f"{random.randint(100000000, 999999999)}{i+40:02d}"
        contact = f"1177777{i:04d}"
        email = f"student{i}@test.com"
        pix = contact
        school = f"Escola {random.choice(['Municipal', 'Estadual', 'Particular'])} {random.randint(1,10)}"
        
        street = f"Rua {random.choice(last_names)}"
        district = "Centro"
        city = "Sao Paulo"
        state = "SP"
        zip_code = "01000000"
        complement = "Apto 1"
        
        f.write(f"INSERT INTO tb_students (id, name, birthdate, cpf, contact, email, pix, role, is_active, created_at, updated_at, school, parent_id, street, district, city, state, zip, complement) VALUES ('{sid}', '{name}', '{birthdate}', '{cpf}', '{contact}', '{email}', '{pix}', 0, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '{school}', '{pid}', '{street}', '{district}', '{city}', '{state}', '{zip_code}', '{complement}');\n")

print("Generated src/main/resources/data.sql")