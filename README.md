# HubSpot Integration API

## Descrição

Este projeto é uma API para integração com o HubSpot, utilizando OAuth2 para autenticação e Webhooks para receber atualizações sobre eventos do HubSpot. Ele permite a criação de contatos no HubSpot e valida a assinatura dos webhooks recebidos para garantir a autenticidade das requisições.

## Funcionalidades

- **Autenticação OAuth2**: O projeto utiliza OAuth2 para autenticação com a API do HubSpot.
- **Criação de Contatos**: Permite criar contatos no HubSpot usando a API.
- **Validação de Webhooks**: Valida a assinatura dos webhooks recebidos, garantindo que são enviados pela HubSpot.

## Tecnologias Usadas

- **Spring Boot**: Framework para construção de aplicações Java baseadas em Spring.
- **Bean Validation**: Usado para validar as entradas da API, garantindo que os dados recebidos nos endpoints estejam no formato correto e sigam as regras definidas
- **WebFlux**: Usado para realizar chamadas assíncronas à API do HubSpot.
- **Jackson**: Usado para manipulação de JSON.
- **Lombok**: Usado para reduzir a verbosidade do código, gerando automaticamente getters, setters, etc.
- **Maven**: Usado para gerenciar as dependencias da aplicação.
- **Ngrok**: Usado para criar um canal de comunicação entre HubSpot e a maquina, trantando ela como um servidor na nuvem.

## Instruções para Execução

### Requisitos iniciais

1. Instalar o [Java 17](https://adoptopenjdk.net/).
2. Criar uma conta free na plataforma do [ngrok](https://ngrok.com/)
3. Criar uma conta free de desenvolvedor na plataforma da [Hubspot](https://app.hubspot.com/login)
4. Criar um aplicativo público com base na documentação da [Hubspot]([https://developers.hubspot.com/docs/guides/apps/public-apps/overview](https://developers.hubspot.com/docs/guides/apps/private-apps/overview))

## Configuração do ambiente

### Variáveis de ambiente

1. Após concluir a criação do seu aplicativo público na plataforma da Hubspot, clique nele.
2. Em seguida acesse a opção "Informações básicas" listada na lateral.
![Imagem Exemplo](Documentation/Images/img0.png)
3. Em seguida acesse a aba "Autenticação".
![Imagem Exemplo](Documentation/Images/img00.png)
4. Ao acessar esta aba, você irá se deparar com a seguinte tela:
![Imagem Exemplo](Documentation/Images/img1.png)
5. Agora vá ate a barra de pesquisa do windows e digite "Editar as variaveis de ambiente do sistema", em seguida clique em "enter"
6. Agora selecione a opção "variaveis de ambiente". Ao clicar nessa opção irá aparecer a seguinte tela:
![Imagem Exemplo](Documentation/Images/img2.png)
7. Agora em "Variaveis de usuário SEU_USUARIO" iremos criar duas variaveis de ambiente, "HUBSPOT_CLIENT_ID" e "HUBSPOT_CLIENT_SECRET"
8. Ainda em "Variaveis de usuário SEU_USUARIO", clique em na opção "Novo..."
9. Ao clicar nessa opção irá aparecer a seguinte imagem:
![Imagem Exemplo](Documentation/Images/img3.png)
10. No campo "nome da variável" você irá preencher com HUBSPOT_CLIENT_ID
11. Agora no campo "valor da variável" iremos colocar o valor que aparecer neste campo aplicativo. No meu caso apareceu esse valor que mostra na imagem, mas cada aplicativo tem o seu, então o valor a ser utilizado será o que estiver no seu aplicativo.
![Imagem Exemplo](Documentation/Images/img4.png)
12. Após preencher os dois campos, clique em "ok"
13 Agora iremos repetir o mesmo processo para criar a variavel de ambiente HUBSPOT_CLIENT_SECRET. HUBSPOT_CLIENT_SECRET será o "nome da variável". Já "valor da variável" iremos utilizar o indicado na imagem. Para ter acesso ao valor, basta clicar em "Mostrar".
![Imagem Exemplo](Documentation/Images/img5.png)
14. Após preencher os dois campos, clique em "ok" para fechar a janela
15. Clique "ok" novamente. Desta vez para fechar a janela de variaveis de ambiente
16. Em seguida, clique em "aplicar" e depois "ok" novamente.
17. Feito tudo isso, reinicie sua maquina para garantir que as variaveis de ambiente sejam capturadas pelo sistema operacional.

### Ngrok

Após ter criado uma conta free na plataforma do [ngrok](https://ngrok.com/) como indicado nos passos iniciais, daremos inicio a instalção e configuração na nossa maquina.

**IMPORTANTE**: Quando for baixar o app do ngrok será necessário desabilitar seu antivirus caso tenha, ou criar uma excessão para o ngrok. Essa ação será necessária pois os antivirus de forma geral interpretam o ngrok como ferramenta maliciosa. Isso se dá pois, muitos hackers o utilizam para criar túneis de acesso a maquina da vitima. Contudo para nossa aplicação ele não será utilizado com esse viez de ataque rsrs. O ngrok é muito útil para testes de APIs, webhooks e desenvolvimento remoto. No caso da nossa aplicação o ngrok será utilizado para criar um túnel seguro para nosso servidor local (localhost), tornando-o acessível a comunicar-se com o servidor da hubspot, que não aceita a rota "localhost" para validação de webhooks.

1. baixe o [ngrok](https://ngrok.com/downloads/windows).
2. Após baixar o ngrok, concender a permissão no antivirus ou desabilitar o antivirus, extraia o arquivo .zip
3. Em seguida, vá até a barra de pesquisa do windows e digite "Windows PowerShell". Deixe ele aberto em segundo plano.
4. Agora acesse sua conta que voce criou no site do [ngrok](https://ngrok.com/). Ao logar, irá aparecer a seguinte tela:
![Imagem Exemplo](Documentation/Images/img6.png)
5. Nas opções listadas a esquerda, acesse "Your Authtoken" ou "Seu Authtoken"
![Imagem Exemplo](Documentation/Images/img7.png)
6. Ao entrar nessa aba, voce irá se deparar com um campo contendo o seu token que autoriza o uso do ngrok na sua maquina
![Imagem Exemplo](Documentation/Images/img8.png)
7. Clique no olho magico para exibir seu token
8. Em seguida, mais abaixo terá o seguinte comando: "ngrok config add-authtoken $YOUR_AUTHTOKEN". Copie o comando.

**IMPORTANTE**: No lugar de $YOUR_AUTHTOKEN, ficará seu token de acesso. Se voce clicou no olho magico para exibir a senha, então automaticamente a variavel em questão será preenchida com o valor do seu token. Exemplo:
![Imagem Exemplo](Documentation/Images/img9.png)
![Imagem Exemplo](Documentation/Images/img10.png)
9. Após copiar o comando, volte para o Powershell e cole o comando.
10. Em seguida aperte "enter".
11. Feito isso, você verá uma mensagem de sucesso confirmando que o token foi salvo em sua maquina. Caso contrário, verifique se o comando foi copiado e colado corretamente como exibido na plataforma
12. Por fim, iremos executar o ngrok. Para isso digite o seguinte comando: "& "C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe" http 8080 --log=stdout".
13. Ao digitar esse comando, aperte "enter". Você deverá ver algo similar a isso:
![Imagem Exemplo](Documentation/Images/img11.png)
**IMPORTANTE**: Atente-se ao caminho do seu ngrok. Se você observar, para o comando funcionar você precisa fornecer a localização exata do seu ngrok em sua maquina, do contrário o comando não irá rodar o ngrok. No meu caso o ngrok esta em C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe
14. Feito isso, ngrok agora está rodando em sua maquina.
**IMPORTANTE**: Após executar o ultimo comando, não feche o powershell ou aperte ctrl+c. Ngrok só cria um túnel de acesso a maquina local durante sua execução. Então caso voce feche o powershell ou mate o processo de execução do ngrok com o ctrl+c, voce terá que executar novamente o comando "& "C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe" http 8080 --log=stdout" para o ngrok voltar a criar o túnel.

### HubSpot - Aplicativo público

Após ter configurado o ngrok localmente, iremos configurar o nosso aplicativo público na plataforma HubSpot

1. Clique no seu aplicativo público.
2. Em seguida acesse a opção "Informações básicas" listada na lateral.
![Imagem Exemplo](Documentation/Images/img0.png)
3. Em seguida acesse a aba "Autenticação".
![Imagem Exemplo](Documentation/Images/img00.png)
4. Ao acessar esta aba, rolando um pouco mais para baixo você irá se deparar com o seguinte campo:
![Imagem Exemplo](Documentation/Images/img12.png)
5. Cole a seguinte url: localhost:8080/api/hubspot/callback
6. Em seguida, rolando mais para baixo você irá se deparar com essa parte:
![Imagem Exemplo](Documentation/Images/img13.png)
7. Agora clique no botão "Adicionar novo escopo"
8. procure pelos escopos: "crm.objects.contacts.read" e "crm.objects.contacts.write".
9. Após encontra-los, selecione ambos e salve-os. Ao fazer isso, eles serão listados como na imagem anterior.
**IMPORTANTE**: Iremos ultiliza-los na função de criar contato, então é fundamental definir essas funções como parte do escopo do nosso aplicativo.
10. Feito isso, na parte inferior da tela irá aparecer a seguinte opção:
![Imagem Exemplo](Documentation/Images/img14.png)
11. Clique em "Salvar alterações"
12. Feito isso, role para cima até aparecer as opções iniciais listadas na barra lateral. Em seguida clique em "Webhooks"
![Imagem Exemplo](Documentation/Images/img15.png)
13. Após entrar na tela de Webhooks, você verá um campo vazio que espera uma url. nesse campo iremos colocar a url que dará acesso ao endpoint da nossa API
14. Incialmente iremos abrir o Powershell que estava em segundo plano
15. Em seguida iremos copiar a url que o comando: "& "C:\Users\User\Documents\João Pedro\Pessoal\ngrok.exe" http 8080 --log=stdout" nos forneceu.
![Imagem Exemplo](Documentation/Images/img16.png)
17. Após copiar a url iremos concatenar ela com seguinte caminho: /api/hubspot/webhook
18. Desse modo, a url final que devemos colocar na plataforma será: SUA_URL_GERADA_NO_NGROK/api/hubspot/webhook. no meu caso ficou https://dea5-170-150-203-129.ngrok-free.app/api/hubspot/webhook
19. Após monstar a url, cole-a no campo que espera a url. Em seguida salve as alterações

**IMPORTANTE**: Essa url é dinamica, ou seja, sempre que voce executar o comando que faz o ngrok rodar, ele irá gerar uma nova url. Por isso destaquei a importancia de deixar o ngrok aberto/minimizado anteriormente. Sempre que voce fechar o powershell, terá que fazer todo esse processo, inclusive o de concatenar a url nova gerada ao restante da url que corresponde a da API.
20. Ao salvar a url no campo. o botão criar assinatura será liberado. clique nele
![Imagem Exemplo](Documentation/Images/img17.png)
21. Ao clicar nele, irá aparecer para você a seguinte aba:
![Imagem Exemplo](Documentation/Images/img18.png)

22. Nela iremos escolher qual objeto queremos e ação queremos que o webhook monitore. Como estamos lidando com criação de contatos, escolheremos no primeiro campo a opção "contato" e no segundo campo a opção "criado".
![Imagem Exemplo](Documentation/Images/img19.png)

24. Agora clique em assinar. Feito isso, sua tela estará assim:
![Imagem Exemplo](Documentation/Images/img20.png)
25. Agora selecione a caixa de dialogo ao lado da palavra "Contato". Ao fazer isso, irá aparecer as opções "ativar" e "pausar". Clique clique em "ativar". Feito isso, seu aplicativo agora passará a monitorar eventos relacionados a criação de contato.

**IMPORTANTE**: Caso voce tenha fechado o powershell e perdido a url do ngrok e precise mudar a url que está no seu aplicativo, voce primeiro precisa pausar a assinatura. Só assim o Hubspot permite com que você a url. Para isso selecione a caixa de dialogo ao lado da palavra "Contato". Ao fazer isso, irá aparecer as opções "ativar" e "pausar". Clique em "pausar". Feito isso voce poderá alterar a url. Ao alterar a url, clique em ativar novamente para restabelecer a função do seu aplicativo de monitoramento

### HubSpot - Conta de teste

Para conseguirmos vizualizar que estamos conseguindo criar os contatos, precisamos ter uma conta de teste responsavel por listar os contatos criados. Para isso iremos criar uma conta de teste de desenvolvedor
