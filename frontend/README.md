# AI Software Engineering Assistant (frontend demo)

Frontend only. No backend, API keys or database. All results are sample data.

## Run
    npm install
    npm run dev      # then open the printed URL
    npm run build

## Where to edit
- src/data/mockData.js        all sample projects, files, chat replies, docs, graph
- src/services/mock.js        mock "API" functions (swap for real calls later)
- src/context/AppContext.jsx  shared state: projects, theme, toasts, confirm dialog
- src/pages/*.jsx             one file per page
- src/components/*.jsx        layout and shared pieces
- src/styles/index.css        theme colors (CSS variables at the top) and all styling
