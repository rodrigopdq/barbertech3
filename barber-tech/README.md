# 💈 BarberTech - Gestão de Agendamentos

Projeto desenvolvido para a disciplina de **Desenvolvimento de Software WEB** da Escola Politécnica, sob orientação do **Prof. Alexandre Cláudio de Almeida**.

## 👤 Identificação
* **Acadêmico:** Rodrigo Pinheiro de Queiroz
* **Tecnologias:** React (Vite), TypeScript, Bootstrap 5.

---

## 🎯 Objetivo do Projeto
O BarberTech é uma aplicação funcional para gestão diária de uma barbearia clássica. O sistema permite visualizar agendamentos de **Corte** e **Barba**, oferecendo um dashboard em tempo real que monitora a produtividade e o faturamento do profissional.

---

## 🏗️ Justificativa da Arquitetura (Critério de Avaliação)

A arquitetura do projeto foi pensada para garantir a **separação de responsabilidades** e a **manutenibilidade** do código:

1.  **Componentização Modular:** A interface foi decomposta em componentes menores (`Sidebar`, `CardServico`, `Footer`). Isso facilita a reutilização e isola a lógica de renderização, permitindo que alterações visuais em um card não afetem a estrutura do dashboard.
2.  **Lifting State Up (Elevação de Estado):** O estado da aplicação (`agendamentos`) foi centralizado no `App.tsx`. Isso garante uma **Single Source of Truth** (Fonte Única de Verdade), onde o componente pai gerencia os dados e os filhos apenas os exibem ou disparam eventos de atualização.
3.  **Segurança com TypeScript:** Foi implementada a interface `IAgendamento` para definir o contrato de dados. O uso de *String Literal Types* para o `tipo` de serviço (Corte | Barba) impede a entrada de dados inválidos e garante a integridade durante o desenvolvimento.
4.  **Layout Assimétrico e Responsivo:** Utilizou-se o sistema de 12 colunas do **Bootstrap**. No Desktop, a aplicação apresenta uma proporção assimétrica de 3/9 (Sidebar/Main), enquanto no Mobile as colunas se empilham em 12 unidades automaticamente via classes utilitárias.

---

## 🛠️ Requisitos Técnicos Implementados

- [x] **Vite + React + TypeScript**: Configuração moderna e rápida.
- [x] **Semântica HTML5**: Uso de tags `aside`, `main`, `header`, `section` e `address`.
- [x] **Bootstrap via CDN**: Estrutura de layout e componentes de UI.
- [x] **Lógica de Estado**: Hook `useState` para atualização dinâmica do Dashboard.
- [x] **CSS Personalizado**: Estilização clássica nas cores azul marinho e vermelho.

---

## 🚀 Como executar o projeto

1. Clone o repositório.
2. Certifique-se de ter o **Node.js** instalado.
3. Execute a instalação das dependências:
   ```bash
   npm install
4. Inicie o servidor de desenvolvimento:
npm run dev
5. Acesse o endereço indicado no terminal (geralmente http://localhost:5173).
