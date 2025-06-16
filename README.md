# Application Layer Load Balancer (Java)

## Overview

This project implements an **HTTP application layer load balancer** in Java, distributing client requests across multiple backend servers. It ensures **high availability**, **fault tolerance**, and **efficient resource utilization** using **round-robin load balancing** and **periodic health checks**.

## Features

- 🖥 **Multi-threaded request handling**
- 🔄 **Round-robin load balancing**
- ✅ **Server health monitoring**
- 🔧 **Dynamic backend server management**
- ⚡ **Concurrent client request processing**

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 11+**
- **Maven or Gradle** (optional, for dependency management)
- Basic understanding of **Java networking and concurrency**

### Installation

1. **Clone the repository**:
   ```sh
   git clone https://github.com/your-username/java-load-balancer.git
   cd java-load-balancer
2. **Compile the project**:
   ```sh
   mvn clean compile
3. **Run the Load Balancer**:
   ```sh
   mvn exec:java -Dexec.mainClass="lb.LoadBalancer"

**🧪 Testing the Load Balancer**
**Start a simple backend server**
  ```sh
  # Backend 1 on port 8080
python -m http.server 8080

