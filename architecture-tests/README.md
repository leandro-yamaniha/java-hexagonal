# Architecture Tests

Este módulo contém testes de arquitetura usando **ArchUnit** para garantir a integridade da arquitetura hexagonal e suas convenções no sistema de gerenciamento de restaurante.

## 📋 Objetivo

Os testes de arquitetura verificam automaticamente se o código está seguindo os princípios da arquitetura hexagonal e as convenções estabelecidas, incluindo:

- **Separação de camadas**: Domain, Application, Infrastructure
- **Direção das dependências**: Regra de dependência da arquitetura limpa
- **Pureza das camadas**: Camadas não devem usar tecnologias inadequadas
- **Convenções de nomenclatura**: Padrões de nomes para classes e pacotes
- **Padrão Ports and Adapters**: Implementação correta de portas e adaptadores

## 🏗️ Estrutura dos Testes

### 1. HexagonalArchitectureTest
Verifica as regras fundamentais da arquitetura hexagonal:

- ✅ **Arquitetura em camadas**: Domain → Application → Infrastructure
- ✅ **Isolamento do domínio**: Domínio não depende de outras camadas
- ✅ **Pureza do domínio**: Sem anotações ou dependências de frameworks
- ✅ **Direção das dependências**: Application só depende de Domain

### 2. NamingConventionTest
Verifica convenções de nomenclatura:

- ✅ **Entidades**: Devem estar no pacote `entity`
- ✅ **Value Objects**: Devem estar no pacote `valueobject`
- ✅ **Repositórios**: Sufixo `Repository`
- ✅ **Use Cases**: Sufixo `UseCase`
- ✅ **Serviços**: Sufixo `Service`
- ✅ **Implementações JPA**: Prefixo `Jpa`
- ✅ **Entidades JPA**: Sufixo `Entity`
- ✅ **Mappers**: Sufixo `Mapper`
- ✅ **Controllers/Resources**: Sufixo `Controller` ou `Resource`

### 3. PortsAndAdaptersTest
Verifica a implementação do padrão Ports and Adapters:

- ✅ **Ports de entrada**: Devem ser interfaces
- ✅ **Ports de saída**: Devem ser interfaces
- ✅ **Serviços de aplicação**: Implementam ports de entrada
- ✅ **Adapters**: Implementam ports de saída
- ✅ **Isolamento**: Controllers só dependem de ports de entrada
- ✅ **Encapsulamento**: Entidades JPA não vazam para outras camadas

### 4. LayerPurityTest
Verifica a pureza e integridade das camadas:

- ✅ **Domínio puro**: Apenas Java padrão e validações
- ✅ **Application limpa**: Sem dependências de infraestrutura
- ✅ **Value Objects imutáveis**: Sem setters
- ✅ **Separação de responsabilidades**: Cada camada com suas dependências permitidas

## 🚀 Executando os Testes

### Executar todos os testes de arquitetura:
```bash
cd architecture-tests
mvn test
```

### Executar teste específico:
```bash
mvn test -Dtest=HexagonalArchitectureTest
mvn test -Dtest=NamingConventionTest
mvn test -Dtest=PortsAndAdaptersTest
mvn test -Dtest=LayerPurityTest
```

### Executar do diretório raiz:
```bash
mvn test -pl architecture-tests
```

## 📊 Relatórios

Os testes geram relatórios detalhados quando falham, mostrando:
- **Violações encontradas**: Classes que violam as regras
- **Localização**: Pacotes e classes específicas
- **Descrição**: Explicação da violação

## 🔧 Configuração

### Dependências principais:
- **ArchUnit**: Framework de testes de arquitetura
- **JUnit 5**: Framework de testes
- **Módulos internos**: Domain, Application, Infrastructure

### Estrutura de pacotes verificada:
```
com.restaurant
├── domain
│   ├── entity
│   └── valueobject
├── application
│   ├── port
│   │   ├── in
│   │   └── out
│   └── service
└── infrastructure
    ├── persistence
    │   ├── entity
    │   ├── repository
    │   └── mapper
    └── cache
```

## 🎯 Benefícios

1. **Detecção precoce**: Violações são detectadas automaticamente
2. **Documentação viva**: Testes servem como documentação da arquitetura
3. **Refatoração segura**: Mudanças não quebram a arquitetura
4. **Onboarding**: Novos desenvolvedores entendem as regras
5. **Qualidade**: Mantém a integridade arquitetural ao longo do tempo

## 🚨 Regras Importantes

### ❌ Proibições do Domínio:
- Anotações JPA (`@Entity`, `@Table`, etc.)
- Anotações Jackson (`@JsonProperty`, etc.)
- Dependências de Spring/Quarkus
- Bibliotecas de infraestrutura (Redis, MySQL, etc.)

### ✅ Permitido no Domínio:
- Java padrão (`java.*`)
- Validações (`jakarta.validation.*`)
- Outras classes do domínio

### 🔄 Fluxo de Dependências:
```
Controllers/Resources → Ports IN → Services → Ports OUT → Adapters
                                      ↓
                                   Domain
```

## 📝 Adicionando Novos Testes

Para adicionar novas regras:

1. Identifique a categoria (arquitetura, nomenclatura, pureza, ports/adapters)
2. Adicione o teste na classe apropriada
3. Use anotação `@DisplayName` descritiva
4. Documente a regra neste README

### Exemplo:
```java
@Test
@DisplayName("Nova regra arquitetural")
void shouldEnforceNewArchitecturalRule() {
    ArchRule newRule = classes()
        .that().resideInAPackage("..domain..")
        .should().notBeAnnotatedWith(SomeAnnotation.class);
    
    newRule.check(importedClasses);
}
```

## 🔍 Troubleshooting

### Teste falhando após mudança:
1. Verifique se a mudança viola alguma regra arquitetural
2. Se a regra não faz mais sentido, atualize o teste
3. Se a mudança está correta, ajuste a implementação

### Performance dos testes:
- Testes carregam todas as classes uma vez (`@BeforeAll`)
- Execução é rápida após carregamento inicial
- Para projetos grandes, considere filtros de pacotes
