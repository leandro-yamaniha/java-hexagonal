# Restaurant Management System - Angular Frontend

A modern, responsive Angular frontend application for the Restaurant Management System built with Angular 17, Angular Material, and NgRx for state management.

## Features

### ✅ Completed Features
- **Dashboard**: Overview with statistics and quick actions
- **Customer Management**: Full CRUD operations with search and filtering
- **Menu Management**: Complete menu item management with categories and availability
- **State Management**: NgRx store with actions, reducers, and effects
- **Responsive Design**: Mobile-first approach with Angular Material
- **Modern UI/UX**: Clean, intuitive interface with Material Design

### 🚧 Planned Features
- **Order Management**: Create, track, and manage orders
- **Table Management**: Visual table layout and reservation system
- **Real-time Updates**: WebSocket integration for live updates
- **Reporting**: Analytics and business intelligence
- **Multi-language Support**: Internationalization (i18n)

## Technology Stack

- **Angular 17**: Latest Angular framework
- **Angular Material**: UI component library
- **NgRx**: State management with Redux pattern
- **RxJS**: Reactive programming
- **TypeScript**: Type-safe JavaScript
- **SCSS**: Enhanced CSS with variables and mixins

## Project Structure

```
src/
├── app/
│   ├── components/           # Feature components
│   │   ├── dashboard/        # Dashboard component
│   │   ├── customers/        # Customer management
│   │   └── menu/            # Menu management
│   ├── models/              # TypeScript interfaces
│   ├── services/            # HTTP services
│   ├── store/               # NgRx store (actions, reducers, effects)
│   ├── modules/             # Lazy-loaded feature modules
│   │   ├── orders/          # Order management module
│   │   └── tables/          # Table management module
│   ├── app.module.ts        # Root module
│   └── app-routing.module.ts # Routing configuration
├── environments/            # Environment configurations
├── styles.scss             # Global styles and Material theme
└── index.html              # Main HTML file
```

## Getting Started

### Prerequisites
- Node.js (v18 or higher)
- npm or yarn
- Angular CLI (`npm install -g @angular/cli`)

### Installation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Start development server**:
   ```bash
   npm start
   # or
   ng serve
   ```

3. **Open browser**:
   Navigate to `http://localhost:4200`

### Build for Production

```bash
npm run build
# or
ng build --configuration production
```

## API Integration

The frontend is configured to connect to the backend API at `http://localhost:8080/api/v1`. Update the environment configuration in `src/environments/environment.ts` to match your backend URL.

### Environment Configuration

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v1'
};
```

## State Management

The application uses NgRx for state management with the following structure:

### Customer Store
- **Actions**: Load, create, update, delete, search customers
- **Reducer**: Manages customer state and loading states
- **Effects**: Handles API calls and side effects

### Menu Store
- **Actions**: Load, create, update, delete menu items, filter by category
- **Reducer**: Manages menu state and availability
- **Effects**: Handles menu API operations

## Components

### Dashboard Component
- Overview statistics
- Quick action buttons
- Recent activity (planned)

### Customer Management
- **CustomerListComponent**: Data table with search, filter, and pagination
- **CustomerFormComponent**: Modal form for create/edit operations

### Menu Management
- **MenuListComponent**: Advanced filtering by category and availability
- **MenuFormComponent**: Comprehensive form with validation

## Styling and Theming

The application uses Angular Material with a custom theme:
- **Primary Color**: Indigo
- **Accent Color**: Pink
- **Warn Color**: Red

Custom styles include:
- Responsive design breakpoints
- Material Design overrides
- Utility classes
- Animation classes

## Development Guidelines

### Code Style
- Follow Angular style guide
- Use TypeScript strict mode
- Implement proper error handling
- Write meaningful component and service names

### State Management
- Use NgRx for complex state
- Implement proper error states
- Handle loading states consistently
- Clear errors appropriately

### UI/UX
- Mobile-first responsive design
- Consistent Material Design patterns
- Proper loading and error states
- Accessible components

## Testing

```bash
# Run unit tests
npm test

# Run tests with coverage
npm run test -- --code-coverage

# Run e2e tests
npm run e2e
```

## Deployment

### Development
```bash
ng serve --host 0.0.0.0 --port 4200
```

### Production
```bash
ng build --configuration production
# Deploy dist/ folder to web server
```

## API Endpoints

The frontend integrates with the following backend endpoints:

### Customers
- `GET /api/v1/customers` - List customers
- `POST /api/v1/customers` - Create customer
- `PUT /api/v1/customers/{id}` - Update customer
- `DELETE /api/v1/customers/{id}` - Delete customer
- `GET /api/v1/customers/search?name={name}` - Search customers

### Menu Items
- `GET /api/v1/menu-items` - List menu items
- `POST /api/v1/menu-items` - Create menu item
- `PUT /api/v1/menu-items/{id}` - Update menu item
- `DELETE /api/v1/menu-items/{id}` - Delete menu item
- `GET /api/v1/menu-items/by-category?category={category}` - Filter by category

## Contributing

1. Follow the established code structure
2. Write comprehensive tests
3. Update documentation
4. Follow Angular and Material Design guidelines
5. Ensure responsive design compatibility

## Architecture Integration

This Angular frontend is part of a comprehensive restaurant management system with:
- **Backend**: Spring Boot / Quarkus with hexagonal architecture
- **Database**: MySQL with Redis caching
- **Infrastructure**: Docker containerization
- **Domain**: Clean architecture with separated concerns

The frontend communicates with the backend through RESTful APIs and maintains its own state using NgRx for optimal user experience and performance.
