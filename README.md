# 💈 BarberTech - Gestão de Barbearia

Projeto desenvolvido para a disciplina de **Desenvolvimento de Software WEB** da Escola Politécnica, sob orientação do **Prof. Alexandre Cláudio de Almeida**.

## 👤 Identificação
* **Acadêmico:** Rodrigo Pinheiro de Queiroz
* **Tecnologias:** React (Vite), TypeScript, Bootstrap 5, Java, Spring Boot, Spring Security, MySQL.

---

## 🎯 Objetivo do Projeto
O BarberTech é uma aplicação funcional para gestão diária de uma barbearia clássica. O sistema permite visualizar agendamentos de **Corte** e **Barba**, oferecendo um dashboard em tempo real que monitora os atendimentos e a produtividade do profissional diretamente integrado ao banco de dados.

---

## 📜 Justificativa da Arquitetura & Critérios Técnicos

A arquitetura do projeto foi pensada para garantir a **separação de responsabilidades** e a **manutenibilidade** do código, atendendo rigorosamente aos critérios de avaliação estabelecidos:

### 1. Organização do Código & Clean Code
* **Front-end (React + TypeScript):** Interface decomposta em componentes modulares e reutilizáveis (`Sidebar`, `CardServico`, `Login` e `ModalAgendamento`). O uso do TypeScript garante tipagem estrita (`IAgendamento`), eliminando bugs em tempo de compilação.
* **Back-end (Java + Spring Boot):** Divisão clara em camadas utilizando os padrões do ecossistema Spring (Controllers, Services, Repositories e DTOs).

### 2. Segurança de Alto Nível (JWT & CORS)
* **Autenticação Stateless:** Controle de acesso baseado em **Tokens JWT**. As senhas dos usuários são criptografadas no banco de dados via **BCrypt**.
* **Axios Interceptors:** Mecanismo implementado no Front-end que captura o token armazenado no `localStorage` após o login bem-sucedido e o injeta automaticamente no cabeçalho `Authorization: Bearer <token>` para todas as requisições subsequentes.
* **Controle de CORS:** Configuração explícita de compartilhamento de recursos de origens cruzadas (`CorsConfigurationSource`) no Spring Security, permitindo que o servidor Java (`localhost:8080`) responda com segurança às chamadas originadas pelo navegador na porta do React (`localhost:5173`).

### 3. Semântica HTTP & Tratamento de Erros
A comunicação entre o cliente e o servidor respeita estritamente as convenções de verbos e códigos de status do protocolo HTTP:
* `GET /agendamento` e `/cliente` -> Consultas de dados efetuadas em chamadas assíncronas paralelas via `Promise.all` para otimizar a performance.
* `POST /auth/login` -> Endpoint público para autenticação de sessão.
* `POST /agendamento/salvar` -> Persistência de novos dados validando o payload.
* `DELETE /agendamento/{id}` -> Remoção semântica do registro no banco MySQL ao concluir o serviço.

---

## 📊 Entidades & Relacionamentos (Fluxo do CRUD)

O sistema cumpre com o requisito de manipulação de entidades relacionadas. A entidade principal **Agendamento** realiza um cruzamento em tempo real de chaves estrangeiras no banco de dados para vincular:
1. Um **Cliente** existente (`clienteId`)
2. Um **Barbeiro** específico (`barbeiroId`)
3. A data e horário desejados (`LocalDateTime`), devidamente tratada no Front-end para o padrão ISO-8601 exigido pelo desserializador do Spring Boot.

---

## 📐 Diagrama do Banco de Dados (ERD)

Abaixo está o modelo relacional das tabelas populadas no MySQL que demonstra a eficácia na manipulação, relacionamento e recuperação de dados:

![Diagrama Entidade Relacionamento](./diagrama-banco.png)

---

## 🏃‍♂️ Como Rodar o Projeto

### Pré-requisitos
* Java JDK 17 ou superior
* MySQL Server ativo
* Node.js instalado

### Execução
1. Certifique-se de que o banco de dados MySQL esteja rodando.
2. Inicialize o Back-end Java através do terminal do projeto:
   ```bash
   ./mvnw spring-boot:run
3.  Inicialize o Front-end React na pasta do projeto:

Bash
npm install
npm run dev


4. Acesse http://localhost:5173/ no Google Chrome e faça a autenticação com as credenciais administrativas.
