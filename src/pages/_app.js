/* eslint-disable @next/next/no-page-custom-font */
/* eslint-disable @next/next/no-css-tags */
import Head from "next/head";
import React, { useEffect } from "react";
import { Provider } from "react-redux";
import "react-responsive-modal/styles.css";
import "./index.scss";
import { store } from "../redux/store";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { PrismicProvider } from "@prismicio/react";
import { PrismicPreview } from "@prismicio/next";
import { linkResolver, repositoryName } from "../services/prismicio";
import Link from "next/link";

if (typeof window !== "undefined") {
  require("bootstrap/dist/js/bootstrap");
}

function MyApp({ Component, pageProps }) {
  useEffect(() => {}, []);
  return (
    <>
      <Head>
        <link
          href="https://fonts.googleapis.com/css2?family=Nunito+Sans:wght@300;400;600;700;800&family=Raleway:wght@300;400;500;600;700;800&display=swap"
          rel="stylesheet"
        />
        <link rel="stylesheet" href="assets/css/fontAwesome5Pro.css" />
        <link rel="stylesheet" href="assets/css/flaticon.css" />
      </Head>

      <PrismicProvider
        linkResolver={linkResolver}
        internalLinkComponent={({ href, children, ...props }) => (
          <Link href={href}>
            <a {...props}>{children}</a>
          </Link>
        )}
      >
        <Provider store={store}>
          <Component {...pageProps} />
          <ToastContainer />
        </Provider>
      </PrismicProvider>
    </>
  );
}

export default MyApp;
