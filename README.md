# HubSpot Integration API

## Descrição

Este projeto é uma API para integração com o HubSpot, utilizando **OAuth2** para autenticação e **Webhooks** para receber atualizações sobre eventos do HubSpot. Ele permite a criação de contatos no HubSpot e valida a assinatura dos webhooks recebidos para garantir a autenticidade das requisições.

## Índice

1. [Funcionalidades](#funcionalidades)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Configuração do ambiente](#configuração-do-ambiente)
   - [Requisitos Iniciais](#requisitos-iniciais)
   - [Variáveis de Ambiente](#variáveis-de-ambiente)
   - [Ngrok](#ngrok)
   - [HubSpot - Aplicativo Público](#hubspot---aplicativo-público)
   - [HubSpot - Conta de Teste](#hubspot---conta-de-teste)
4. [Execução da API](#execução-da-api)
5. [Melhorias Futuras](#melhorias-futuras)

## Funcionalidades

- **Autenticação OAuth2**: O projeto utiliza OAuth2 para autenticação com a API do HubSpot.
- **Criação de Contatos**: Permite criar contatos no HubSpot usando a API.
- **Validação de Webhooks**: Valida a assinatura dos webhooks recebidos, garantindo que são enviados pela HubSpot.

## Tecnologias Utilizadas

- **Spring Boot**: Framework utilizado para construção de aplicações Java baseadas em Spring.
- **Bean Validation**: Biblioteca utilizada para validar as entradas da API, garantindo que os dados recebidos nos endpoints estejam no formato correto e sigam as regras definidas.
- **WebFlux**: Biblioteca utilizada para realizar chamadas assíncronas à API do HubSpot.
- **Jackson**: Biblioteca utilizada para manipulação de JSON.
- **Lombok**: Biblioteca utilizada para reduzir a verbosidade do código, gerando automaticamente getters, setters, etc.
- **Maven**: Biblioteca utilizada para gerenciar as dependências da API.
- **Ngrok**: Biblioteca utilizada para criar um túnel seguro entre o nosso servidor local (localhost) e o servidor da HubSpot.

## Configuração do ambiente

### Requisitos Iniciais

1. Instalar o [Java 17](https://adoptopenjdk.net/).
2. Criar uma conta gratuita na plataforma do [ngrok](https://ngrok.com/).
3. Criar uma conta gratuita de desenvolvedor na plataforma da [HubSpot](https://app.hubspot.com/login).
4. Criar um aplicativo público com base na documentação da [HubSpot](https://developers.hubspot.com/docs/guides/apps/public-apps/overview).
5. Instalar algum ferramenta para consumo de API. Sugestão: [Postman](https://www.postman.com/downloads/).

### Variáveis de Ambiente

1. Após concluir a criação do seu aplicativo público na plataforma da HubSpot, clique nele.
2. Acesse a opção **"Informações básicas"** listada na lateral.
   ![Imagem Exemplo](Documentation/Images/img0.png)
3. Em seguida, acesse a aba **"Autenticação"**.
   ![Imagem Exemplo](Documentation/Images/img00.png)
4. Na aba **Autenticação**, você verá a seguinte tela:
   ![Imagem Exemplo](Documentation/Images/img1.png)
5. Agora, vá até a barra de pesquisa do Windows e digite **"Editar as variáveis de ambiente do sistema"**, depois pressione **Enter**.
6. Clique na opção **"Variáveis de ambiente"**. Ao clicar nessa opção, aparecerá a seguinte tela:
   ![Imagem Exemplo](Documentation/Images/img2.png)
7. Em **"Variáveis de usuário"**, crie duas variáveis de ambiente: **HUBSPOT_CLIENT_ID** e **HUBSPOT_CLIENT_SECRET**.
8. Clique em **Novo...** para adicionar a variável.
   ![Imagem Exemplo](Documentation/Images/img3.png)
9. No campo **"Nome da variável"**, preencha com **HUBSPOT_CLIENT_ID**.
10. No campo **"Valor da variável"**, coloque o valor exibido no seu aplicativo HubSpot (veja a imagem como referência).
   ![Imagem Exemplo](Documentation/Images/img4.png)
11. Clique em **OK**.
12. Repita o processo para criar a variável **HUBSPOT_CLIENT_SECRET**, usando o valor indicado na interface da HubSpot.
   ![Imagem Exemplo](Documentation/Images/img5.png)
13. Clique em **OK** novamente para fechar a janela de variáveis de ambiente.
14. Clique em **Aplicar** e depois em **OK** para finalizar a configuração.
15. Após isso, reinicie a máquina para garantir que as variáveis de ambiente sejam capturadas pelo sistema operacional.

### Ngrok

Após criar uma conta gratuita na plataforma do [ngrok](https://ngrok.com/), conforme indicado nos passos iniciais, vamos iniciar a instalação e configuração do ngrok na nossa máquina.

**IMPORTANTE**: Durante o processo de download e instalação do ngrok, será necessário desabilitar o antivírus, caso você tenha um, ou criar uma exceção para o ngrok. Isso ocorre porque muitos antivírus interpretam o ngrok como uma ferramenta maliciosa. Isso se deve ao fato de que, em algumas situações, hackers usam o ngrok para criar túneis de acesso remoto à máquina da vítima. No entanto, para a nossa API, o ngrok será utilizado de maneira legítima, sem qualquer viés de ataque. O ngrok é extremamente útil para testes de APIs, webhooks e desenvolvimento remoto. No nosso caso, ele será usado para criar um túnel seguro entre o nosso servidor local (localhost) e o servidor da HubSpot, permitindo a comunicação com a HubSpot, que não aceita a rota "localhost" para validação de webhooks.

1. Baixe o [ngrok](https://ngrok.com/downloads/windows).
2. Após o download, conceda permissão ao antivírus ou desative-o temporariamente. Extraia o arquivo `.zip` baixado.
3. Em seguida, vá até a barra de pesquisa do Windows e digite **"Windows PowerShell"**. Deixe-o aberto em segundo plano.
4. Acesse sua conta no site do [ngrok](https://ngrok.com/). Ao fazer login, você verá a seguinte tela:
   ![Imagem Exemplo](Documentation/Images/img6.png)
5. No menu à esquerda, acesse a opção **"Your Authtoken"** (ou "Seu Authtoken").
   ![Imagem Exemplo](Documentation/Images/img7.png)
6. Você será direcionado para uma página que exibirá o seu token, que autoriza o uso do ngrok na sua máquina.
   ![Imagem Exemplo](Documentation/Images/img8.png)
7. Clique no ícone do "olho mágico" para exibir seu token.
8. Em seguida, será exibido o comando para configurar o token na sua máquina: `ngrok config add-authtoken $YOUR_AUTHTOKEN`. Copie esse comando.

**IMPORTANTE**: No lugar de **$YOUR_AUTHTOKEN**, ficará o seu token de acesso. Se você clicou no ícone do "olho mágico" para exibir a senha, automaticamente a variável será preenchida com o valor do seu token. Veja o exemplo abaixo:
   ![Imagem Exemplo](Documentation/Images/img9.png)
   ![Imagem Exemplo](Documentation/Images/img10.png)
9. Após copiar o comando, volte para o **PowerShell** e cole o comando copiado.

10. Pressione **Enter**. Se tudo estiver correto, você verá uma mensagem de sucesso confirmando que o token foi salvo na sua máquina. Caso contrário, verifique se o comando foi copiado e colado corretamente.

11. Por fim, iremos executar o ngrok. Para isso digite o seguinte comando: `"& "C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe" http 8080 --log=stdout"`.

12. Ao digitar o comando, pressione Enter. Você deverá ver algo semelhante a isso:
![Imagem Exemplo](Documentation/Images/img11.png)
**IMPORTANTE**: Atente-se ao caminho do seu ngrok. Se você observar, para o comando funcionar você precisa fornecer a localização exata do seu ngrok em sua máquina, do contrário o comando não irá rodar o ngrok. No meu caso, o ngrok está localizado em C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe.

13. Feito isso, o ngrok agora estará rodando em sua máquina.

**IMPORTANTE**: Após executar o último comando, não feche o PowerShell ou aperte Ctrl+C. O ngrok só cria o túnel de acesso à máquina local durante sua execução. Então, caso você feche o PowerShell ou interrompa o processo de execução do ngrok com Ctrl+C, será necessário executar novamente o comando:

### HubSpot - Aplicativo Público

Após ter configurado o ngrok localmente, iremos configurar o nosso aplicativo público na plataforma HubSpot.

1. Clique no seu aplicativo público.
2. Em seguida, acesse a opção **"Informações básicas"** listada na lateral.
   ![Imagem Exemplo](Documentation/Images/img0.png)
3. Em seguida, acesse a aba **"Autenticação"**.
   ![Imagem Exemplo](Documentation/Images/img00.png)
4. Ao acessar esta aba, rolando um pouco mais para baixo, você irá se deparar com o seguinte campo:
   ![Imagem Exemplo](Documentation/Images/img12.png)
5. Cole a seguinte URL: `localhost:8080/api/hubspot/callback`.
6. Em seguida, role mais para baixo e você irá se deparar com essa parte:
   ![Imagem Exemplo](Documentation/Images/img13.png)
7. Agora, clique no botão **"Adicionar novo escopo"**.
8. Procure pelos escopos: **"crm.objects.contacts.read"** e **"crm.objects.contacts.write"**.
9. Após encontrá-los, selecione ambos e salve-os. Ao fazer isso, eles serão listados como na imagem anterior.

   **IMPORTANTE**: Iremos utilizá-los na função de criar contato, então é fundamental definir esses escopos como parte do nosso aplicativo.

10. Feito isso, na parte inferior da tela, irá aparecer a seguinte opção:
    ![Imagem Exemplo](Documentation/Images/img14.png)
11. Clique em **"Salvar alterações"**.
12. Após salvar, role para cima até aparecer as opções iniciais listadas na barra lateral. Em seguida, clique em **"Webhooks"**.
    ![Imagem Exemplo](Documentation/Images/img15.png)
13. Após entrar na tela de **Webhooks**, você verá um campo vazio que espera uma URL. Nesse campo, iremos colocar a URL que dará acesso ao endpoint da nossa API.
14. Inicialmente, abra o **PowerShell** que estava em segundo plano.
15. Em seguida, copie a URL gerada pelo comando: `& "C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe" http 8080 --log=stdout`.
    ![Imagem Exemplo](Documentation/Images/img16.png)
16. Após copiar a URL, iremos concatená-la com o seguinte caminho: `/api/hubspot/webhook`.
17. Dessa forma, a URL final que devemos colocar na plataforma será: `SUA_URL_GERADA_NO_NGROK/api/hubspot/webhook`. No meu caso, ficou `https://dea5-170-150-203-129.ngrok-free.app/api/hubspot/webhook`.
18. Após montar a URL, cole-a no campo que espera a URL. Em seguida, salve as alterações.

**IMPORTANTE**: Essa URL é dinâmica, ou seja, sempre que você executar o comando que faz o ngrok rodar, ele irá gerar uma nova URL. Por isso, é importante deixar o ngrok aberto/minimizado. Sempre que você fechar o PowerShell, terá que fazer todo esse processo novamente, incluindo a atualização da URL gerada.

19. Após salvar a URL no campo, o botão **"Criar assinatura"** será liberado. Clique nele.
    ![Imagem Exemplo](Documentation/Images/img17.png)
20. Ao clicar nele, aparecerá a seguinte aba:
    ![Imagem Exemplo](Documentation/Images/img18.png)
21. Nela, iremos escolher qual objeto e qual ação queremos que o webhook monitore. Como estamos lidando com a criação de contatos, escolheremos no primeiro campo a opção **"Contato"** e no segundo campo a opção **"Criado"**.

    ![Imagem Exemplo](Documentation/Images/img19.png)
22. Agora, clique em **Assinar**. Feito isso, sua tela estará assim:
    ![Imagem Exemplo](Documentation/Images/img20.png)
23. Agora, selecione a caixa de diálogo ao lado da palavra **"Contato"**. Ao fazer isso, aparecerão as opções **"Ativar"** e **"Pausar"**. Clique em **"Ativar"**. Feito isso, seu aplicativo agora passará a monitorar eventos relacionados à criação de contatos.

**IMPORTANTE**: Caso você tenha fechado o PowerShell e perdido a URL do ngrok e precise alterar a URL no seu aplicativo, você precisa primeiro pausar a assinatura. Só assim o HubSpot permitirá a alteração da URL. Para isso, selecione a caixa de diálogo ao lado da palavra **"Contato"**. Ao fazer isso, aparecerão as opções **"Ativar"** e **"Pausar"**. Clique em **"Pausar"**. Feito isso, você poderá alterar a URL. Após alterar, clique em **Ativar** novamente para restabelecer a função do seu aplicativo de monitoramento.

### HubSpot - Conta de Teste

Para conseguirmos visualizar que estamos conseguindo criar os contatos, precisamos ter uma conta de teste responsável por listar os contatos criados. Para isso, iremos criar uma conta de teste de desenvolvedor.

1. Para isso, acesse a seguinte opção localizada no menu à esquerda da página:
   ![Imagem Exemplo](Documentation/Images/img21.png)
2. Ao acessar esta opção, você se deparará com a seguinte tela. Clique no botão **"Criar uma conta de teste de desenvolvedor"**.
   ![Imagem Exemplo](Documentation/Images/img22.png)
3. Ao clicar no botão, aparecerá um pop-up. Nesse pop-up, você irá denominar um nome para sua conta de teste.
   ![Imagem Exemplo](Documentation/Images/img23.png)
4. Feito isso, clique em **"Criar"**.
5. Prontinho! Agora temos todo o ambiente configurado para executarmos nossa API!

---

## Execução da API

Agora vamos ao que interessa, que é ver essa belezinha funcionando!!

1. Após concluir todos os passos anteriores, baixe o projeto e importe-o para a sua IDE.
2. Feito isso, rode o projeto.
   
   **IMPORTANTE**: Não esqueça, **não pode fechar o PowerShell**. Caso o feche, o ngrok não será executado. Se você tiver fechado o PowerShell, abra-o novamente e execute os passos descritos anteriormente para executá-lo e configurar a URL na aba de webhooks do seu aplicativo.
   
3. Com a API executando, vá até seu navegador e acesse a URL: `http://localhost:8080/api/hubspot/authorize`.
4. Em seguida, aperte **Enter**. Deverá aparecer a seguinte tela:
   ![Imagem Exemplo](Documentation/Images/img24.png)
5. Agora selecione a conta de teste que você criou.
6. Em seguida, clique em **"Escolher conta"**.
   ![Imagem Exemplo](Documentation/Images/img25.png)
7. Ao fazer isso, a API lhe encaminhará para esta tela:
   ![Imagem Exemplo](Documentation/Images/img26.png)
8. Agora, clique no botão **"Conectar aplicativo"**. Ele dará acesso à sua conta de teste ao seu aplicativo da HubSpot.
9. Em seguida, a HubSpot irá gerar um token de acesso:
   ![Imagem Exemplo](Documentation/Images/img27.png)
10. Copie este token.

   **IMPORTANTE**: Quando for copiar o token, caso apareça um `\` no final do token, **não o copie**. Esse caractere é apenas um delimitador e não faz parte do token em si, mesmo estando dentro das aspas onde o token está.

11. Agora, abra o seu **Postman**.
12. Crie uma requisição do tipo **POST**.
13. Em seguida, faça a seguinte configuração na requisição, como nas imagens a seguir:
   
   13.1. Copie e cole na URL da sua requisição o seguinte caminho: `http://localhost:8080/api/hubspot/create-contact`.
   
   13.2. Acesse a aba **"Headers"**. Como mostrado na imagem, você irá criar duas chaves para seu cabeçalho (**Authorization** e **Content-Type**). Na chave **"Authorization"**, coloque o token gerado no campo **value**. Já na chave **"Content-Type"**, você irá colocar o valor como `application/json`:
   ![Imagem Exemplo](Documentation/Images/img33.png)

   13.3. Agora, clique na aba **"Body"**.
   
   13.4. Em seguida, clique em **"raw"**.
   
   13.5. Na mesma linha, na última opção, clique e escolha a opção **"JSON"**.

   ![Imagem Exemplo](Documentation/Images/img34.png)
   
   13.6. Agora crie um **JSON** que passe as informações necessárias para criar um contato. Exemplo:
   
   ```json
   {
       "firstName": "marcos",
       "lastName": "paulo",
       "email": "marcos.doe@example.com"
   }
 ```
14. Feito isso, clique no botão **"Send"** para disparar a função de criar contato.
15. Agora, acesse sua conta de teste para validarmos se o contato foi criado.
16. Para isso, volte até a plataforma da HubSpot e acesse a seguinte opção localizada no menu à esquerda da página:
   ![Imagem Exemplo](Documentation/Images/img21.png)
17. Você verá a conta que criamos anteriormente. Agora, basta clicar nela que você será redirecionado para a página da sua conta de teste.

18. Quando estiver na tela da sua conta de teste, acesse a opção **CRM > Contatos**, localizada no menu à esquerda da página:
    
   ![Imagem Exemplo](Documentation/Images/img32.png)
   
19. Ao acessar essa opção, você verá que seu contato foi criado.
   ![Imagem Exemplo](Documentation/Images/img35.png)
20. Feito isso, voltaremos para nosso aplicativo para validarmos se ele monitorou a criação de contato e se validou a assinatura da API que tentou executar essa função.
21. Para isso, acesse a opção **Monitoramento** listada à esquerda:
   ![Imagem Exemplo](Documentation/Images/img36.png)
22. Ao acessar, você logo de cara verá uma lista que mostrará todas as chamadas realizadas ao nosso aplicativo. Dentre elas, estará nossa requisição que fizemos no Postman para tentar criar o contato:
   ![Imagem Exemplo](Documentation/Images/img28.png)
23. Em seguida, vamos validar se o **Webhook** monitorou essa tentativa de criar o contato e se ele validou a assinatura da API que tentou executar essa função. Para isso, clique na aba **Webhooks**:
   ![Imagem Exemplo](Documentation/Images/img37.png)
24. Como pode ser observado, foi tudo um sucesso. O webhook não só detectou a tentativa como também validou a assinatura de quem tentou criar o contato, no caso, nossa API:
   ![Imagem Exemplo](Documentation/Images/img29.png)
25. Para acessar mais detalhes relacionados ao processo que o webhook monitorou, basta clicar no item listado:
   ![Imagem Exemplo](Documentation/Images/img30.png)
   ![Imagem Exemplo](Documentation/Images/img31.png)

## Melhorias Futuras:

### 1. Documentação da API
Atualmente, não há uma documentação visível da API. Pensando em um possível crescimento dela, o ideal seria utilizar ferramentas como Swagger ou Spring REST Docs para gerar uma documentação interativa da API. Isso ajudaria os desenvolvedores a entenderem melhor os endpoints disponíveis e como interagir com eles.

### 2. Organização e Modularização
À medida que o projeto crescer, pode ser necessário refatorar algumas responsabilidades para diferentes módulos ou pacotes a fim de manter o código limpo e organizado.
Exemplo: No caso do projeto atual, talvez no futuro seja necessário modularizar ainda mais a API, separando as responsabilidades de forma mais detalhada. Isso pode incluir mover SignatureService, OAuthService e ContactService para pacotes separados com interfaces e implementações distintas.

### 3. Testes Automatizados
Não há menção de testes automatizados no projeto. Essa decisão foi tomada devido ao fato de o projeto ser pequeno. Contudo, à medida que o projeto crescer, seria interessante implementar testes unitários e testes de integração. Ambos ajudariam a garantir que a lógica da API permanecesse sólida e funcional.

### 4. Respostas de erro mais especificas e informativas
Apesar do projeto ter uma classe global que captura os erros e os informa ao usuário, ela ainda é bem genérica. O ideal seria expandir essa classe para capturar uma variedade maior de erros, com mensagens personalizadas mais detalhadas e informativas, proporcionando uma experiência mais clara e útil para o usuário.

### 5. Segurança da API (Autenticação e Autorização)
Por se tratar de um projeto que só permite o uso das suas funções principais (como /create-contact e /webhooks) através de um token validado em uma plataforma externa (HubSpot), não se viu a necessidade de implementar o Spring Security na API. Contudo, essa decisão pode mudar dependendo do crescimento e da evolução das necessidades da API no futuro.

