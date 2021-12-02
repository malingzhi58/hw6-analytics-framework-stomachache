module.exports = {
  extends: [
    'airbnb',
    'airbnb-typescript',
    'airbnb/hooks',
    'plugin:@typescript-eslint/recommended',
  ],
  parserOptions: {
    project: './tsconfig.eslint.json',
  },
  rules: {
    'no-plusplus': 0,
    'no-await-in-loop': 0,
    'no-console': 0,
    'spaced-comment': 0,
    'object-curly-newline': 0,
    'react/no-array-index-key': 0,
    'react/require-default-props': 0,
    'react/jsx-props-no-spreading': 0,
    'max-len': 0,
  },
};
