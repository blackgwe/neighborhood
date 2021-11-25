# Dev instructions 


[CRUD & Quarkus](https://dvddhln.medium.com/creating-a-crud-app-with-quarkus-reactive-hibernate-panache-and-graphql-using-a-postgresql-216ecd75ee52)

###  Postgres – docker container

```bash
docker compose up
# docker run -d --rm --name my_reative_db \
#       -e POSTGRES_USER=user \
#       -e POSTGRES_PASSWORD=password 
#       -e POSTGRES_DB=my_db 
#       -p 5432:5432 postgres:11.5

# clean
docker rm $(docker ps -a -q) -f
docker volume prune
```

### Run / Build

```bash
# live coding
./mvnw compile quarkus:dev

# native image
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

GraphQL UI http://localhost:8080/q/graphql-ui/

### GraphQL examples

```json
query allOrganisations {
  allOrganisations {
    title
    createdDate
    zipCode
    id
    products {
      name
    }
  }
}

query getOrganisation {
  organisation(organisationId: 1) {
    title
    zipCode
    createdDate
  }
}

query getOrganisationAndProductName {
  organisation(organisationId: 1) {
    title
    zipCode
    createdDate
    products {
      name
    }
  }
}

mutation addOrganisation {
  createOrganisation(
    organisation: {title: "The Mask", createdDate: "1994-12-25", zipCode: "99098"}
  ) {
    title
    createdDate
    zipCode
  }
}

mutation updateOrganisation {
  updateOrganisation(organisationId: 1, organisation: {title: "The One"}) {
    title
  }
}

mutation deleteOrganisation {
  deleteOrganisation(organisationId: 1)
}

query allProducts {
  allProducts {
    name
    organisations {
      title
      zipCode
    }
  }
}

mutation addProductToOrganisation {
  addProductToOrganisation(organisationId: 1, productId: 4) {
    title
    createdDate
    zipCode
    products {
      name
    }
  }
}

mutation addOrganisationToProduct {
  addOrganisationToProduct(organisationId: 2, productId: 4) {
    name
    id
    organisations {
      title
      products {
        name
      }
    }
  }
}


```

