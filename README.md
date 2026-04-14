📦 Sistema de Almoxarifado

Sistema de gerenciamento de materiais desenvolvido com foco em controle de estoque, rastreabilidade e automação de processos internos.

🚀 Tecnologias utilizadas
Java + Spring Boot
JPA / Hibernate
MySQL
HTML, CSS, JavaScript
API REST
Fetch (integração front-end)
🎯 Funcionalidades

✔ Cadastro de materiais
✔ Edição de status (Disponível, Em uso, Indisponível)
✔ Exclusão de materiais
✔ Busca por nome
✔ Upload de imagens
✔ Geração de QR Code para identificação
✔ Controle de movimentação de itens
✔ Integração completa entre front-end e back-end

📊 Regras de Negócio
Quando o material está DISPONÍVEL → sem responsável
Quando está EM USO → obrigatório informar o colaborador
Quando está INDISPONÍVEL → não pode ser utilizado
🧠 Arquitetura

O sistema segue o padrão:

Controller → recebe requisições HTTP
Service → regras de negócio
Repository → acesso ao banco de dados
DTO → transferência de dados
🔗 Endpoints principais
📥 Listar materiais

GET /v1/Warehouse/show_list

🔍 Buscar por nome

POST /v1/Warehouse/filter

{
  "name": "martelo"
}
➕ Adicionar material

POST /v1/Warehouse/add_product

{
  "name": "Furadeira",
  "typeofmoviment": "EXIT",
  "status": "AVAILABLE"
}
✏️ Editar status

PATCH /v1/Warehouse/editar/status

{
  "id": 1,
  "state": "IN_USE",
  "collaboratorName": "João"
}
🗑️ Deletar material

DELETE /v1/Warehouse/delete/{id}

📷 Upload de imagem

POST /v1/Warehouse/upload

📱 Gerar QR Code

GET /v1/Warehouse/qrcode/{id}

💻 Como rodar o projeto
1. Clone o repositório
git clone https://github.com/seuusuario/almoxarifado.git
2. Configure o banco de dados (MySQL)

Crie um banco e configure no application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/seubanco
spring.datasource.username=root
spring.datasource.password=senha
3. Execute a aplicação
mvn spring-boot:run
4. Acesse o sistema
http://localhost:8080
📌 Melhorias futuras
🔒 Autenticação com login (JWT)
📊 Dashboard com métricas
📱 Versão mobile
📜 Histórico completo de movimentações
🔔 Notificações de uso
👨‍💻 Autor

Luan Gustavo Fidelis
🔗 LinkedIn: https://linkedin.com/in/luan-fidelis-09442b284

# ⭐ Observação

Este sistema foi desenvolvido com objetivo de aplicação real em um almoxarifado, sendo utilizado para controle de materiais e processos internos. O projeto segue em constante evolução, buscando atender demandas reais e aplicar boas práticas de desenvolvimento utilizadas no mercado.


.
