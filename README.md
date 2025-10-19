# Cofre Digital McPato

**Cofre Digital McPato** é uma aplicação console de simulação bancária e corretora de ativos, desenvolvida em Java, com foco em segurança, modularidade e experiência didática para o curso de Engenharia de Software da FIAP.

---

## 🚀 Sobre o Projeto

Este sistema foi criado como atividade e laboratório prático para a turma de Engenharia de Software da FIAP. O objetivo é exercitar conceitos avançados de orientação a objetos, design modular, autenticação segura e operações bancárias em múltiplos ativos, dentro de um ambiente “fullstack” orientado a serviços.

---

## ✨ Funcionalidades

- Cadastro de pessoas físicas (CPF) e jurídicas (CNPJ)
- Login com autenticação em dois fatores (2FA)
- Compra e venda de ativos digitais, com cotação dinâmica
- Saques e depósitos simulados, além de transferências de ativos entre contas
- Relatórios detalhados: extrato de operações, transferências, saldo de carteira e ativos
- Modularização: serviços dedicados para autenticação, operação, transferência, gestão de ativos e relatórios

---

## 🛠 Estrutura dos Arquivos

src/main/java/<br>
├── model/<br>
│   ├── Account.java<br>
│   ├── Asset.java<br>
│   ├── Operation.java<br>
│   └── Transfer.java<br>
├── service/<br>
│   ├── AuthService.java<br>
│   ├── AssetService.java<br>
│   ├── OperationService.java<br>
│   ├── TransferService.java<br>
│   ├── ReportService.java<br>
│   └── TwoFactorService.java<br>
└── Main.java<br>


---

## 💡 Destaques Técnicos

- **Separação de responsabilidades:** Nenhuma regra de negócio fica no Main — tudo nos services.
- **Segurança:** Hash de senha e autenticação 2FA simulada.
- **Arquitetura limpa:** Fácil evolução e reuso em outros projetos.
- **Formatação:** Valores sempre exibidos com duas casas decimais e ponto como separador decimal.

---

## 🏁 Como executar

1. Clone este repositório: `git clone https://github.com/seu-usuario/cofre-digital-mcpato.git`
2. Importe na sua IDE Java preferida (IntelliJ, Eclipse, VS Code).
3. Certifique-se de usar Java 17 ou superior.
4. Compile e execute `Main.java`.

---

## 📙 Exemplos de Uso

- **Cadastro:** Opção 1 no menu inicial
- **Login:** Opção 2 + 2FA
- **Operar ativos:** Comprar ou vender ativos digitais com atualização de preço a cada operação
- **Movimentações:** Saques, depósitos e transferências de ativos entre clientes cadastrados
- **Relatórios:** Extrato completo e detalhado disponível em qualquer momento pelo menu

---

