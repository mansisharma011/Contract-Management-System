# Contract Management System

A Spring Boot backend project for managing legal contracts in a consultancy workflow. The system supports contract upload, document parsing, contract retrieval, status updates, file download, and basic contract Q&A using keyword matching.

## Features

- Upload PDF/DOCX contracts
- Update existing contracts
- Store contract metadata in MongoDB
- Store uploaded contract files locally
- Extract contract text using Apache Tika
- Fetch all contracts
- Fetch contract details by ID
- Download uploaded contract file
- Update contract status
- Ask basic questions from contract content using keyword matching
- Global exception handling
- Request validation support

## Contract Workflow

```text
DRAFT → REVIEW → APPROVED
```

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data MongoDB
- MongoDB
- Maven
- Apache Tika
- Lombok
- Spring Validation

## Project Structure
Built using layered architecture with separation of concerns.

```text
src/main/java/com/contractmanagementsystem
├── controller
├── dto
├── exception
├── model
├── repository
├── service
├── utils
└── ContractManagementSystemApplication.java
```

## API Endpoints

### Client APIs

```http
POST /client
```

Upload a new contract.

```http
PUT /client/{id}
```

Update an existing contract.

### Consultant APIs

```http
GET /Consultant
```

Fetch all contracts.

```http
GET /Consultant/{id}
```

Fetch contract details by ID.

```http
GET /Consultant/{id}/file
```

Download uploaded contract file.

```http
POST /Consultant/{id}/ask
```

Ask a question from the extracted contract text.

```http
PUT /Consultant/updateStatusToReview/{id}
```

Update contract status from `DRAFT` to `REVIEW`.

```http
PUT /Consultant/updateStatusToApproved/{id}
```

Update contract status from `REVIEW` to `APPROVED`.

## Q&A Request Example

Request body type: `text/plain`

```text
What are the payment terms?
```

Example response:

```json
{
  "contractId": "contract-id",
  "contractName": "Rent Contract",
  "question": "What are the payment terms?",
  "answer": "Matched contract content...",
  "score": 2
}
```

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/mansisharma011/Contract-Management-System.git
cd Contract-Management-System
```

### 2. Start MongoDB locally

Make sure MongoDB is running on your system.

### 3. Configure database

Update `src/main/resources/application.properties` if required:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/contract_db
```

### 4. Run the application

For Linux/macOS:

```bash
./mvnw spring-boot:run
```

For Windows:

```bash
mvnw.cmd spring-boot:run
```

The application will run at:

```text
http://localhost:8080
```

## Document Processing

Uploaded contract files are stored locally, and their text content is extracted using Apache Tika. The extracted text is used for contract search and keyword-based Q&A retrieval.

## Error Handling

The project includes a global exception handler for validation errors, contract-related exceptions, and text extraction errors.

## Future Improvements

- Add authentication and authorization
- Add AI based Q&A handling
- Add Swagger/OpenAPI documentation
- Add unit and integration tests
- Add pagination and filtering
- Add cloud-based file storage

## Author

Developed by [Mansi Sharma](https://github.com/mansisharma011)
