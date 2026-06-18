# Contract Management System

A Spring Boot backend application designed to manage legal contracts through a secure consultancy workflow.

The system implements role-based and ownership-based access control to ensure that users can only access contracts they are authorized to view. Clients can manage their own contracts, consultants can review and approve contracts assigned to them, and administrators can oversee workflow progress without accessing confidential contract content.

In addition to contract management, the application supports document parsing using Apache Tika and provides contract question-answering through keyword-based retrieval from extracted contract text.

---

## Overview

Legal contracts often contain sensitive information that should only be accessible to specific stakeholders. This project focuses on enforcing those access boundaries throughout the entire contract lifecycle.

Key security rules include:

  - Clients can only access, update, download, and query their own contracts.
  - Consultants can only access contracts assigned to them.
  - Administrators can monitor contract workflow and user activity without viewing contract contents.
  - Every contract retrieval request validates both user role and contract ownership/assignment before data is returned.
  - Unauthorized access attempts result in application-level exceptions and access denial.

Beyond access control, the system supports:

- Secure JWT-based authentication
- Contract upload and storage
- Document text extraction using Apache Tika
- Contract lifecycle management (Draft → Review → Approved)
- Contract question-answering using keyword-based retrieval
- File download and document management
- MongoDB-based persistence

---

## Key Features

### Authentication & Security

- JWT-based Authentication
- Spring Security Integration
- Stateless Session Management
- Role-Based Access Control (RBAC)
- Ownership-based contract access validation
- Users can only access resources permitted by their role and ownership rules

Supported Roles:

- ADMIN
- CONSULTANT
- CLIENT

---

### Contract Management

- Upload PDF/DOCX contracts
- Update contract details
- Retrieve contracts
- Download uploaded contract files
- Track and Update contract status
- Store metadata in MongoDB
- Store uploaded files locally

---

### Document Processing

- Text extraction using Apache Tika
- Extracted content stored for retrieval
- Contract Question & Answer functionality
- Keyword-based answer retrieval from extracted contract text

---

### Workflow Management

Contracts move through the following lifecycle:

```text
DRAFT
   ↓
REVIEW
   ↓
APPROVED
```

This workflow ensures contracts are reviewed and approved before completion.

---

## Security Architecture

```text
   Client Request
         │
         ▼
Spring Security Filter Chain
         │
         ▼
JWT Authentication Filter
         │
         ▼
    Security Context
         │
         ▼
Role & Ownership Validation
         │
         ▼
   Controller Layer
         │
         ▼
    Service Layer
         │
         ▼
MongoDB / File Storage
```

---

## Role Permissions

### Admin

- View contracts status across the system
- View consultants
- View clients
- Assign consultant roles
- Monitor contract workflow

### Consultant

- View assigned contracts
- Review assigned contracts
- Approve assigned contracts
- Download assigned contract files
- Ask questions from assigned contract content

### Client

- Upload contracts
- Update own contracts
- View own contracts
- Download own contracts
- Ask questions from own uploaded contracts

---

## Document Processing Flow for Q&A

```text
Upload Contract
       │
       ▼
Store File Locally
       │
       ▼
Apache Tika Extraction
       │
       ▼
Store Extracted Text
       │
       ▼
Contract Q&A Retrieval
```

---

## Technology Stack

### Backend

- Java 21
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Validation

### Database

- MongoDB
- Spring Data MongoDB

### Document Processing

- Apache Tika

### Build Tools

- Maven
- Lombok

### Authentication

- JWT (JSON Web Token)

---

## Project Structure

```text
src/main/java/com/contractmanagementsystem
├── controller
├── dto
├── exception
├── model
├── repository
├── security
├── service
├── utils
└── ContractManagementSystemApplication.java
```

---

## Sample API Groups

### Authentication

```http
POST /auth/register
POST /auth
```

### Client Operations

```http
POST /client
PUT /client/{id}
GET /client
GET /client/{id}
POST /client/{id}/ask
```

### Consultant Operations

```http
GET /consultant
GET /consultant/{id}
PUT /consultant/updateStatusToReview/{id}
PUT /consultant/updateStatusToApproved/{id}
```

### Admin Operations

```http
GET /admin/getAllContracts
PUT /admin/{id}
```

---

## Question & Answer Example

### Request

```text
What are the payment terms?
```

### Response

```json
{
  "contractId": "contract-id",
  "contractName": "Rent Contract",
  "question": "What are the payment terms?",
  "answer": "Matched contract content...",
  "score": 2
}
```

---

## Setup & Installation

### Clone Repository

```bash
git clone https://github.com/mansisharma011/Contract-Management-System.git
cd Contract-Management-System
```

### Configure MongoDB

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/contract_db
```

### Configure JWT Secret

```properties
jwt.secret=YOUR_BASE64_SECRET_KEY
```

### Run Application

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

## Future Improvements

- LLM-powered Contract Q&A using Retrieval-Augmented Generation (RAG)
- Semantic Search using Vector Embeddings
- OpenAPI / Swagger Documentation
- Unit & Integration Testing
- Pagination & Filtering
- Cloud File Storage (AWS S3)
- Contract Versioning
- Audit Logging


---

## Author

**Mansi Sharma**

Java Backend Developer | Spring Boot | MongoDB | Spring Security | REST APIs
