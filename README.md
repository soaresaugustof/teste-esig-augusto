# Desafio Técnico Java - Gestão e Cálculo de Salários

Este projeto é uma aplicação web Java completa desenvolvida como solução para o desafio técnico da ESIG. A aplicação gere uma base de dados de funcionários, cargos e vencimentos, e implementa a lógica de negócio para calcular e consolidar os salários de cada pessoa.

A arquitetura segue uma abordagem modular que separa as responsabilidades entre a camada de apresentação (View), a camada de controle (Beanss), a camada de serviço (Lógica de Negócio) e a camada de acesso a dados (DAOs).

## Funcionalidades Implementadas

### Requisitos Obrigatórios
- **Listagem Consolidada:** Uma tabela de dados principal que exibe todas as pessoas com os seus salários já calculados, incluindo funcionalidades de ordenação, paginação e filtros.
- **Cálculo de Salários:** Uma ação que lê os dados de origem (pessoas, cargos, vencimentos), executa a lógica de negócio para somar os créditos e subtrair os débitos, e preenche a tabela consolidada com os resultados.

### Diferenciais
- **Processamento Assíncrono:** O cálculo de salários é executado em segundo plano, libertando a interface do utilizador imediatamente e atualizando a tabela de forma automática quando o processo termina, garantindo uma melhor experiência do usuário.
- **CRUD de Pessoas:** Um módulo completo para gerir os registos da tabela `pessoa`.
- **Sistema de Autenticação e Registro:** Uma solução de segurança com tela de login, tela de registo e um filtro de autorização que protege as páginas da aplicação. Inclui também um CRUD para que os administradores possam gerir as contas dos usuários.
- **Relatórios em PDF:** Funcionalidade para exportar a listagem de salários consolidada para PDF, utilizando JasperReports e populando o relatório a partir de uma coleção de objetos Java.
- **Testes de Unidade:** Uma classe de testes automatizados com JUnit 5 e Mockito para validar a lógica de negócio principal do `SalarioService`, garantindo a qualidade e a manutenibilidade do código.

## Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Servidor de Aplicação:** Apache Tomcat 9.0.x
- **Web Framework:** JavaServer Faces (JSF) 2.3
- **Componentes de UI:** PrimeFaces 11.0.0 e OmniFaces 3.13
- **Injeção de Dependência:** CDI 2.0 (Weld Implementation)
- **Persistência de Dados:** JPA 2.2 (Hibernate 5.6 Implementation)
- **Banco de Dados:** PostgreSQL
- **Build & Dependency Management:** Apache Maven
- **Relatórios:** JasperReports 6.20.0
- **Testes:** JUnit 5 (Jupiter) & Mockito 4.8

---

## Guia de Instalação e Execução

### 1. Pré-requisitos

Certifique-se de que tem o ambiente tenha:
- **JDK 11** ou superior
- **Apache Maven** 3.6+
- **PostgreSQL**
- **Apache Tomcat 9.0.x**
- Uma IDE Java, como o **IntelliJ IDEA** ou o Eclipse.

### 2. Configuração do Banco de Dados

1.  **Crie o Banco de Dados:** Abra o seu gestor de base de dados (DBeaver, pgAdmin, etc.) e execute o seguinte comando SQL para criar a base de dados:
    ```sql
    CREATE DATABASE teste_esig_augusto
        WITH 
        OWNER = postgres
        ENCODING = 'UTF8';
    ```
    *Ajuste o `OWNER` se o seu utilizador principal do PostgreSQL for diferente.*

2.  **Criação das Tabelas (Automática):** A aplicação está configurada com `hibernate.hbm2ddl.auto=update`. Isto significa que, da primeira vez que a aplicação arrancar, o Hibernate irá **criar automaticamente todas as tabelas necessárias** com base nas entidades Java.

3.  **Importe a Massa de Dados Inicial:**
    * **Separe o Excel:** Converta cada folha de cálculo do ficheiro Excel original em ficheiros `.csv` separados (`Pessoa.csv`, `Cargo.csv`, `Vencimentos.csv`, `Cargo_Vencimentos.csv`).
    * **Importe os CSVs:** Use o assistente de importação de CSV do seu gestor de base de dados para popular as tabelas. **Atenção à ordem de importação** para respeitar as chaves estrangeiras:
        1.  `Cargo.csv` -> tabela `cargo`
        2.  `Vencimentos.csv` -> tabela `vencimentos`
        3.  `Pessoa.csv` -> tabela `pessoa`
        4.  `Cargo_Vencimentos.csv` -> tabela `cargo_vencimentos`
    * **Configurações de Importação Cruciais:**
        * Marque a opção **"First row is header" (A primeira linha é o cabeçalho)**.
        * Certifique-se de que o **"Encoding"** está definido para **UTF-8**.
        * Verifique o **mapeamento das colunas** para garantir que cada coluna do CSV corresponde à coluna correta na tabela.
        * Para a tabela `pessoa`, no campo de data, defina o formato como `MM/dd/yyyy`:
        * ```sql
          ALTER USER postgres SET datestyle TO 'ISO, MDY';
          ```
          
### 3. Configuração do Projeto

1.  **Clone o Repositório:** `git clone https://github.com/soaresaugustof/teste-esig-augusto.git`
2.  **Abra no IntelliJ IDEA:** Abra o projeto como um projeto Maven.
3.  **Atualize a Senha do Banco:** Abra o ficheiro `src/main/resources/META-INF/persistence.xml` e altere a propriedade `javax.persistence.jdbc.password` para a sua senha do PostgreSQL (caso o usuário não seja o postgres, altere o campo user também).
    ```xml
    <property name="javax.persistence.jdbc.password" value="sua_senha_aqui" />
    ```
4.  **Configure o Servidor Tomcat:**
    * Vá a `Run > Edit Configurations...`.
    * Adicione uma nova configuração "Tomcat Server > Local".
    * Aponte para a pasta de instalação do seu Tomcat.
    * Na aba "Deployment", adicione o artefacto `TesteTecnicoESIGAugusto:war exploded`.
    * No campo "Application context", defina o caminho como `/TesteTecnicoESIGAugusto`.

### 4. Executar a Aplicação

1.  No IntelliJ, clique no botão **"Run"** (Play) para iniciar o servidor Tomcat.
2.  Após o deploy, a aplicação estará acessível no seguinte URL:
    * **`http://localhost:8080/TesteTecnicoESIGAugusto/login.xhtml`**
3.  Faça o registro de uma conta e comece a testar todas as funcionalidades.
