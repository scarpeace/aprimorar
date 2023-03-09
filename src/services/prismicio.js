import * as prismic from "@prismicio/client";

export const endpoint = "https://aprimorar.prismic.io/api/v2";
const acessToken =
  "MC5ZcXZwYUJFQUFDUUFoSGto.77-977-977-977-9Ve-_ve-_vX3vv701Ie-_ve-_ve-_vRvvv704aO-_vUgxNe-_vVEwQO-_vUrvv73vv71477-9";
export const repositoryName = prismic.getRepositoryName(endpoint);

export function linkResolver(doc) {
  switch (doc.type) {
    default:
      return null;
  }
}

export function createClient(config = {}) {
  const client = prismic.createClient(endpoint, {
    acessToken,
  });

  return client;
}
