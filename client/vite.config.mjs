import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';
import jsconfigPaths from 'vite-jsconfig-paths';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const API_URL = `${env.VITE_APP_BASE_NAME}`;
  const PORT = '3000'; // No need for template literals here

  return {
    server: {
      open: true,  // Automatically opens the browser on server start
      port: PORT,  // Set the server port to 3000
    },
    define: {
      global: 'window', // Define global variable for the application
    },
    resolve: {
      alias: [
        // Add any aliases here if needed
      ],
    },
    css: {
      preprocessorOptions: {
        scss: {
          charset: false,
        },
        less: {
          charset: false,
        },
      },
      charset: false,
      postcss: {
        plugins: [
          {
            postcssPlugin: 'internal:charset-removal',
            AtRule: {
              charset: (atRule) => {
                if (atRule.name === 'charset') {
                  atRule.remove();
                }
              },
            },
          },
        ],
      },
    },
    base: API_URL, // Base URL for the app
    plugins: [react(), jsconfigPaths()], // React and JS Config paths support
  };
});
