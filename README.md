# 🚀 Consultor de Carreira Híbrido (Java + IA) - Global Solution FIAP 2025

Olá! Este é o nosso projeto para a **Global Solution 2025 (2º Semestre) da FIAP**.

O tema desse semestre foi o "Futuro do Trabalho", e nossa solução é um aplicativo desktop (feito em Java) que ajuda as pessoas a se adaptarem a esse mundo novo. A ideia é ser um "Consultor de Carreira" que ajuda o usuário a encontrar uma profissão e a descobrir o que precisa estudar para chegar lá.

1.  Algoritmo Local: Tem um sistema de recomendação "offline" que fizemos do zero, usando Java puro, para cumprir todos os requisitos da matéria de DDD.
2.  IA do Google: Ele também se conecta de verdade com a API do Google Gemini para dar sugestões e insights gerados por IA, refinando mais ainda nosso projeto.

---

👥 Nosso Time

| Nome | RM | GitHub |
| :--- | :--- | :--- |
| Leonardo Fernandes Mesquita | 559623 | [leoGitFiap](https://github.com/leoGitFiap) |
| Marco Antonio Caires Freire | 559256 | [MACF77](https://github.com/MACF77) |

---

## Link do Repositório: https://github.com/leoGitFiap/GS_2_Semestre_2025_DDD

---

## 🎯 Como o Aplicativo Funciona?

O app é bem direto. Você começa na `TelaInicial`, clica em "Começar" e vai para a `TelaSelecao`. Lá, você tem dois caminhos:

### 1. Botão "Gerar (Algoritmo Local)"
Este botão foi feito para cumprir os requisitos da matéria de DDD:

* Ele usa o nosso algoritmo `RecomendacaoService.java`.
* A gente criou um "banco de dados" de carreiras dentro do código (`List<Carreira> BASE`).
* O algoritmo usa um **sistema de pontos** (`score()`) para comparar a sua escolha com as carreiras que a gente cadastrou.
* Ele sempre acha a "melhor" opção, mesmo que não seja 100% igual ao que você pediu (é o que chamamos de *fallback*).
* Aí, ele te leva para a `TelaResultado`, que mostra a carreira, o salário, os cursos e as *skills* que estão no nosso banco de dados.

### 2. Botão "Perguntar à IA"
Este é o lado "inovador" do projeto para a GS:

* Ele usa o `GeminiApiService.java`.
* Ele pega as suas escolhas (Área, Especialidade, Nicho) e transforma em uma pergunta (um *prompt*) para a IA do Google.
* O app se conecta na API do Gemini, envia a pergunta e...
* Mostra a resposta da IA em um pop-up!

---

## 🛠️ Como Rodar o Projeto (Infos Técnicas)

O projeto foi todo feito em Java Swing (para a interface) e segue uma arquitetura limpa, separando o código em pastas (pacotes): `model` (os dados), `service` (a lógica) e `ui` (as telas).

Para rodar o projeto 100%, você vai precisar de dois requisitos:

#### 1. A Biblioteca de JSON
A gente usa a `org.json` para o Java conseguir montar a pergunta e ler a resposta da IA. O jeito mais fácil de adicionar no IntelliJ é:
1.  Vá em `File` > `Project Structure...` > `Libraries`.
2.  Clique no `+` e escolha `From Maven...`.
3.  Na busca, digite `org.json:json` e dê OK. O IntelliJ baixa e instala sozinho. - OU - Escolha a opção `Java` e selecione o arquivo `json-20250517.jar` presente na pasta `2_2025_GS_DDD`
#### 2. A Chave da API do Google
Sem isso, o botão da IA não funciona (ele vai dar um erro de "chave não configurada").
1.  **Crie sua chave:** Vá no [Google AI Studio](https://aistudio.google.com/) (é de graça) e gere uma nova chave de API.
2.  **Cole a chave:** Abra o arquivo `src/service/GeminiApiService.java` e cole a chave que você gerou lá na linha 17, na variável `API_KEY`.

---
**Pronto!**

Depois disso, é só encontrar o arquivo `src/ui/Main.java` e clicar no "play" (▶) para rodar o aplicativo.