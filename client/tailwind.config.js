/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        beige: {
          background: "#efe7da", 
          offwhite: "#ffffe4",  
        },
        green: {
          lightPastel: "#add0b3",
        },
        brown: {
          lightPastel: "#d2b48c", 
        },
      },
      // Add more custom colors as needed
    },
  },
  plugins: [],
};

